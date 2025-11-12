
package practicos.practico_09_integrador.src;

import estructuras.mapa.HashMapPersonalizado;
import estructuras.lista_enlazada.ListaEnlazada;
import java.time.LocalDateTime;

public class GestorDeTurnos implements MapaPacientes {
    private static final int CAPACIDAD_INICIAL = 16;

    private HashMapPersonalizado<String, Paciente> indicePacientes;
    private HashMapPersonalizado<String, Medico> indiceMedicos;
    private HashMapPersonalizado<String, Turno> turnosPorId;
    private HashMapPersonalizado<String, AgendaMedico> agendasPorMedico;

    public GestorDeTurnos() {
        this.indicePacientes = new HashMapPersonalizado<>(CAPACIDAD_INICIAL);
        this.indiceMedicos = new HashMapPersonalizado<>(CAPACIDAD_INICIAL);
        this.turnosPorId = new HashMapPersonalizado<>(CAPACIDAD_INICIAL);
        this.agendasPorMedico = new HashMapPersonalizado<>(CAPACIDAD_INICIAL);
    }

    // MapaPacientes implementation (delegan a indicePacientes)
    @Override
    public void put(String dni, Paciente p) {
        indicePacientes.poner(dni, p);
    }

    @Override
    public Paciente get(String dni) {
        return indicePacientes.obtener(dni);
    }

    @Override
    public boolean remove(String dni) {
        return indicePacientes.eliminar(dni);
    }

    @Override
    public boolean containsKey(String dni) {
        return indicePacientes.obtener(dni) != null;
    }

    @Override
    public int size() {
        return indicePacientes.contar();
    }

    // Aquí se implementarán las operaciones del gestor (agendar/cancelar/buscar), usando las estructuras creadas.

    /**
     * Carga inicial de datos desde CSVs (pacientes.csv, medicos.csv, turnos.csv).
     * Se asume la existencia de una clase helper CSVParser.parse(String path) -> List<String[]>.
     */
    public void cargarDatosIniciales() {
        // Cargar pacientes
        try {
            java.util.List<String[]> filasPacientes = CSVParser.parse("datasets/pacientes.csv");
            for (String[] row : filasPacientes) {
                if (row == null || row.length < 2) continue;
                String dni = row[0].trim();
                String nombre = row[1].trim();
                Paciente p = new Paciente(dni, nombre);
                indicePacientes.poner(dni, p);
            }
        } catch (Exception e) {
            System.err.println("Error leyendo pacientes.csv: " + e.getMessage());
        }

        // Cargar medicos y crear agendas
        try {
            java.util.List<String[]> filasMedicos = CSVParser.parse("datasets/medicos.csv");
            for (String[] row : filasMedicos) {
                if (row == null || row.length < 3) continue;
                String matricula = row[0].trim();
                String nombre = row[1].trim();
                String especialidad = row[2].trim();
                Medico m = new Medico(matricula, nombre, especialidad);
                indiceMedicos.poner(matricula, m);
                // Inicializar agenda del médico
                agendasPorMedico.poner(matricula, new AgendaMedicoImpl());
            }
        } catch (Exception e) {
            System.err.println("Error leyendo medicos.csv: " + e.getMessage());
        }

        // Cargar turnos con validaciones
        try {
            java.util.List<String[]> filasTurnos = CSVParser.parse("datasets/turnos.csv");
            for (String[] row : filasTurnos) {
                if (row == null || row.length < 6) continue;
                String id = row[0].trim();
                String dniPaciente = row[1].trim();
                String matriculaMedico = row[2].trim();
                String fechaHoraStr = row[3].trim();
                String duracionStr = row[4].trim();
                String motivo = row[5].trim();

                // Validaciones
                if (turnosPorId.obtener(id) != null) {
                    System.err.println("Turno " + id + " rechazado: id duplicado");
                    continue;
                }

                if (indicePacientes.obtener(dniPaciente) == null) {
                    System.err.println("Turno " + id + " rechazado: paciente no encontrado");
                    continue;
                }

                if (indiceMedicos.obtener(matriculaMedico) == null) {
                    System.err.println("Turno " + id + " rechazado: medico no encontrado");
                    continue;
                }

                LocalDateTime fechaHora = null;
                try {
                    fechaHora = LocalDateTime.parse(fechaHoraStr);
                } catch (Exception ex) {
                    System.err.println("Turno " + id + " rechazado: formato fecha invalido -> " + fechaHoraStr);
                    continue;
                }

                if (fechaHora.isBefore(LocalDateTime.now())) {
                    System.err.println("Turno " + id + " rechazado: fecha pasada");
                    continue;
                }

                int duracionMin = 0;
                try {
                    duracionMin = Integer.parseInt(duracionStr);
                    if (duracionMin <= 0) {
                        System.err.println("Turno " + id + " rechazado: duracion invalida");
                        continue;
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println("Turno " + id + " rechazado: duracion no es numero");
                    continue;
                }

                // Si todo OK, crear Turno y guardarlo
                Turno t = new Turno(id, dniPaciente, matriculaMedico, fechaHora, duracionMin, motivo);
                turnosPorId.poner(id, t);
                AgendaMedico agenda = agendasPorMedico.obtener(matriculaMedico);
                if (agenda == null) {
                    // debería no ocurrir si medicos.csv fue cargado correctamente, pero lo manejamos
                    agenda = new AgendaMedicoImpl();
                    agendasPorMedico.poner(matriculaMedico, agenda);
                }
                boolean agendado = agenda.agendar(t);
                if (!agendado) {
                    System.err.println("Turno " + id + " no pudo agendarse en la agenda del medico " + matriculaMedico);
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo turnos.csv: " + e.getMessage());
        }
    }

    // Accesores públicos para uso por la UI (Main)
    public AgendaMedico getAgenda(String matricula) {
    return agendasPorMedico.obtener(matricula);
    }

    public Paciente getPaciente(String dni) {
    return indicePacientes.obtener(dni);
    }

    /**
     * Retorna la lista de turnos del médico (inorder) o null si no existe agenda.
     */
    public ListaEnlazada<Turno> listarTurnosMedico(String matricula) {
    AgendaMedico agenda = agendasPorMedico.obtener(matricula);
        if (agenda == null) return null;
        if (agenda instanceof AgendaMedicoImpl) {
            AgendaMedicoImpl impl = (AgendaMedicoImpl) agenda;
            return impl.avl.obtenerEnOrden();
        }
        // si es otra implementación, intentar vía reflexión o devolver lista vacía
        return new ListaEnlazada<>();
    }

    public boolean agregarTurno(Turno t) {
        if (t == null) return false;
        String id = t.getId();
        if (id == null || id.isEmpty()) return false;
    if (turnosPorId.obtener(id) != null) return false; // id duplicado
    AgendaMedico agenda = agendasPorMedico.obtener(t.getMatriculaMedico());
        if (agenda == null) {
            agenda = new AgendaMedicoImpl();
            agendasPorMedico.poner(t.getMatriculaMedico(), agenda);
        }
        boolean ok = agenda.agendar(t);
        if (ok) {
            turnosPorId.poner(id, t);
        }
        return ok;
    }

    public Turno getTurnoById(String id) {
    return turnosPorId.obtener(id);
    }

    public boolean cancelarTurno(String id) {
        if (id == null) return false;
    Turno t = turnosPorId.obtener(id);
        if (t == null) return false;
    AgendaMedico agenda = agendasPorMedico.obtener(t.getMatriculaMedico());
        if (agenda == null) return false;
        boolean ok = agenda.cancelar(id);
        if (ok) {
            turnosPorId.eliminar(id);
        }
        return ok;
    }
}
