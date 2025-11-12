
package practicos.practico_09_integrador.src;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import estructuras.lista_enlazada.ListaEnlazada;
import estructuras.monticulo_binario.MinHeapGenerico;
import estructuras.mapa.HashMapPersonalizado;

/**
 * Planificador simple de quirófanos.
 * Algoritmo: heap (min-heap) con la próxima disponibilidad por quirófano.
 * Para cada solicitud se asigna el quirófano con disponibilidad más temprana
 * y se intenta programar en la misma jornada; si no cabe se desplaza al día siguiente.
 *
 * Suposiciones razonables tomadas por la implementación:
 * - No se valida la disponibilidad del médico en sus agendas existentes (puede añadirse).
 * - Las solicitudes se procesan en el orden recibido.
 * - Las jornadas se repiten todos los días entre fechaInicio y fechaFin inclusive.
 */
public class PlanificadorQuirofano {

    private static class RoomSlot implements Comparable<RoomSlot> {
        int roomId;
        LocalDateTime availableAt;

        RoomSlot(int roomId, LocalDateTime availableAt) {
            this.roomId = roomId;
            this.availableAt = availableAt;
        }

        @Override
        public int compareTo(RoomSlot other) {
            return this.availableAt.compareTo(other.availableAt);
        }

        @Override
        public String toString() {
            return "RoomSlot{roomId=" + roomId + ", availableAt=" + availableAt + "}";
        }
    }

    /**
     * Planifica las solicitudes en los quirófanos disponibles y devuelve un mapa (MiHashMap)
     * que asocia número de quirófano (1..numQuirafanos) con la lista de Turnos programados.
     *
     * @param solicitudes lista de solicitudes (matrícula médico + duración en minutos)
     * @param numQuirafanos cantidad de quirófanos disponibles
     * @param fechaInicio primera fecha en la que se puede programar (inclusive)
     * @param fechaFin última fecha válida para programar (inclusive)
     * @param inicioJornada hora de inicio diaria (ej. 08:00)
     * @param finJornada hora de fin diaria (ej. 18:00)
     * @return MiHashMap<Integer, ListaEnlazada<Turno>> con la planificación por quirófano
     */
    public HashMapPersonalizado<Integer, ListaEnlazada<Turno>> planificar(ListaEnlazada<SolicitudCirugia> solicitudes,
                                                      int numQuirafanos,
                                                      LocalDate fechaInicio,
                                                      LocalDate fechaFin,
                                                      LocalTime inicioJornada,
                                                      LocalTime finJornada) {
    HashMapPersonalizado<Integer, ListaEnlazada<Turno>> resultado = new HashMapPersonalizado<Integer, ListaEnlazada<Turno>>(Math.max(4, numQuirafanos));

        if (solicitudes == null || solicitudes.contar() == 0 || numQuirafanos <= 0) {
            return resultado;
        }

        // Heap de disponibilidad de quirófanos
    MinHeapGenerico<RoomSlot> heap = new MinHeapGenerico<>();

        // Inicializar quirófanos con fechaInicio a la hora de inicio de jornada
        for (int i = 1; i <= numQuirafanos; i++) {
            LocalDateTime start = fechaInicio.atTime(inicioJornada);
            heap.insertar(new RoomSlot(i, start));
            resultado.poner(i, new ListaEnlazada<>());
        }

        int seq = 1; // para generar ids únicos de turno

        for (SolicitudCirugia req : solicitudes) {
            int dur = req.getDuracionMin();
            boolean scheduled = false;

            // Intentar asignar la solicitud buscando el primer quirófano que pueda alojarla
            // avanzando días si es necesario, pero sin pasar de fechaFin
            int attempts = 0;
            // Simple protection: no más que (numQuirafanos * número de días + 10)
            long maxDays = fechaFin.toEpochDay() - fechaInicio.toEpochDay() + 1;
            int maxAttempts = (int) Math.max( (long)numQuirafanos * Math.max(1L, maxDays) + 10, 1000);

            while (!scheduled && attempts < maxAttempts && !heap.estaVacio()) {
                RoomSlot slot = heap.extraerMin();
                if (slot == null) break;

                // Si la disponibilidad está antes del inicio de jornada, subirla al inicio
                if (slot.availableAt.toLocalTime().isBefore(inicioJornada)) {
                    slot.availableAt = slot.availableAt.toLocalDate().atTime(inicioJornada);
                }

                // Si la disponibilidad está fuera de la ventana diaria (después del fin), mover al siguiente día inicio
                if (slot.availableAt.toLocalTime().isAfter(finJornada) || slot.availableAt.toLocalTime().equals(finJornada)) {
                    LocalDate siguiente = slot.availableAt.toLocalDate().plusDays(1);
                    slot.availableAt = siguiente.atTime(inicioJornada);
                }

                // Si la duración no entra en la jornada actual del slot, mover al siguiente día
                LocalDateTime endOfDay = slot.availableAt.toLocalDate().atTime(finJornada);
                LocalDateTime possibleEnd = slot.availableAt.plusMinutes(dur);
                if (possibleEnd.isAfter(endOfDay)) {
                    // mover al inicio del siguiente día
                    LocalDate siguiente = slot.availableAt.toLocalDate().plusDays(1);
                    slot.availableAt = siguiente.atTime(inicioJornada);
                    // comprobar si superamos fechaFin
                    if (slot.availableAt.toLocalDate().isAfter(fechaFin)) {
                        // Este quirófano no podrá programar más en el rango; lo descartamos (no lo reinsertamos)
                        attempts++;
                        continue;
                    } else {
                        // reinsertar y seguir intentando
                        heap.insertar(slot);
                        attempts++;
                        continue;
                    }
                }

                // Si la fecha del slot ya está fuera del rango permitido
                if (slot.availableAt.toLocalDate().isAfter(fechaFin)) {
                    // No hay más espacio en fecha límite en este quirófano
                    attempts++;
                    continue;
                }

                // Programar aquí
                String id = "Q" + slot.roomId + "-" + (seq++);
                String dniPaciente = ""; // no tenemos paciente en la solicitud general
                Turno t = new Turno(id, dniPaciente, req.getMatriculaMedico(), slot.availableAt, dur, "Cirugia - Quir\u00f3fano " + slot.roomId);

                // Añadir al resultado
                ListaEnlazada<Turno> lista = resultado.obtener(slot.roomId);
                if (lista == null) {
                    lista = new ListaEnlazada<>();
                    resultado.poner(slot.roomId, lista);
                }
                lista.insertarFinal(t);

                // Avanzar la disponibilidad del quirófano
                slot.availableAt = slot.availableAt.plusMinutes(dur);

                // Si la nueva disponibilidad cae en el mismo día después del fin, mover al siguiente día inicio
                if (slot.availableAt.toLocalTime().isAfter(finJornada) || slot.availableAt.toLocalTime().equals(finJornada)) {
                    LocalDate siguiente = slot.availableAt.toLocalDate().plusDays(1);
                    slot.availableAt = siguiente.atTime(inicioJornada);
                }

                // Reinsertar el slot para futuras asignaciones (si aún dentro de fechaFin)
                if (!slot.availableAt.toLocalDate().isAfter(fechaFin)) {
                    heap.insertar(slot);
                }

                scheduled = true;
            }

            if (!scheduled) {
                System.err.println("No se pudo programar la solicitud del medico " + req.getMatriculaMedico() + " (dur=" + dur + "min) dentro del rango solicitado.");
            }
        }

        return resultado;
    }

    // Helper wrapper: procesa una única solicitud con parámetros por defecto y la retorna el plan
    public HashMapPersonalizado<Integer, ListaEnlazada<Turno>> procesar(SolicitudCirugia solicitud) {
        ListaEnlazada<SolicitudCirugia> list = new ListaEnlazada<>();
        list.insertarFinal(solicitud);
        // parámetros por defecto razonables
        int numQuirafanos = 2;
        LocalDate hoy = LocalDate.now();
        LocalDate fin = hoy.plusDays(7);
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime finJ = LocalTime.of(18, 0);
    return planificar(list, numQuirafanos, hoy, fin, inicio, finJ);
    }

    /**
     * Placeholder: devuelve lista vacía (no se computa bloqueo real en esta versión).
     */
    public ListaEnlazada<String> topKMedicosBloqueados(int k) {
        return new ListaEnlazada<>();
    }
}
