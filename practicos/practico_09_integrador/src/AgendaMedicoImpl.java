
package practicos.practico_09_integrador.src;
import estructuras.arbol_AVL.ArbolAVLGenerico;
import estructuras.lista_enlazada.ListaEnlazada;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Implementación de AgendaMedico basada en MiArbolAVL<Turno>.
 */
public class AgendaMedicoImpl implements AgendaMedico {
    protected final ArbolAVLGenerico<Turno> avl;

    public AgendaMedicoImpl() {
        this.avl = new ArbolAVLGenerico<>();
    }

    @Override
    public boolean agendar(Turno t) {
        if (t == null || t.getFechaHora() == null) return false;

        // comprobar solapamientos
        Turno siguiente = avl.mayorOIgual(t); // primer turno >= t
        Turno anterior = avl.menorQue(t); // mayor turno < t

        if (anterior != null) {
            LocalDateTime finAnterior = anterior.getFechaHora().plusMinutes(anterior.getDuracionMin());
            if (finAnterior.isAfter(t.getFechaHora())) {
                return false; // overlap con anterior
            }
        }

        if (siguiente != null) {
            LocalDateTime finT = t.getFechaHora().plusMinutes(t.getDuracionMin());
            if (siguiente.getFechaHora().isBefore(finT)) {
                return false; // overlap con siguiente
            }
        }

        // sin conflictos: insertar
        avl.insertar(t);
        return true;
    }

    @Override
    public boolean cancelar(String idTurno) {
        if (idTurno == null) return false;
        ListaEnlazada<Turno> lista = avl.obtenerEnOrden();
        for (Turno t : lista) {
            if (idTurno.equals(t.getId())) {
                avl.eliminar(t);
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Turno> siguiente(LocalDateTime t) {
        if (t == null) return Optional.empty();
        // crear un Turno temporal para buscar en el AVL
        Turno dummy = new Turno("", "", "", t, 0, "");
        Turno res = avl.mayorOIgual(dummy);
        return Optional.ofNullable(res);
    }

    @Override
    public ListaEnlazada<Turno> getTurnosOrdenados() {
        // MiArbolAVL ya provee obtenerEnOrden() que devuelve la lista en orden por fecha
        return avl.obtenerEnOrden();
    }

    /**
     * Encuentra el primer hueco >= durMin minutos comenzando desde t0.
     * Busca en la agenda y luego en días sucesivos dentro del horario laboral 9:00-18:00.
     */
    public Optional<LocalDateTime> primerHueco(LocalDateTime t0, int durMin) {
        if (t0 == null) return Optional.empty();
        if (durMin <= 0) return Optional.of(t0);

        LocalDateTime cursor = t0;
        final LocalTime workStart = LocalTime.of(9, 0);
        final LocalTime workEnd = LocalTime.of(18, 0);

        // limit search to reasonable number of days to avoid infinite loop
        for (int dayOffset = 0; dayOffset < 30; dayOffset++) {
            LocalDate date = cursor.toLocalDate();
            LocalDateTime dayStart = LocalDateTime.of(date, workStart);
            LocalDateTime dayEnd = LocalDateTime.of(date, workEnd);

            LocalDateTime searchFrom = cursor.isAfter(dayStart) ? cursor : dayStart;

            // obtener todos los turnos a partir de searchFrom
            ListaEnlazada<Turno> lista = avl.obtenerEnOrden();

            LocalDateTime prevEnd = searchFrom;
            for (Turno turno : lista) {
                LocalDateTime inicio = turno.getFechaHora();
                LocalDateTime fin = inicio.plusMinutes(turno.getDuracionMin());

                if (fin.isBefore(searchFrom)) continue; // turno anterior al inicio de búsqueda
                if (inicio.isAfter(dayEnd)) break; // ya pasamos el día

                if (inicio.isAfter(prevEnd) || inicio.isEqual(prevEnd)) {
                    long gap = java.time.Duration.between(prevEnd, inicio).toMinutes();
                    if (gap >= durMin && !prevEnd.isBefore(dayStart) && !prevEnd.isAfter(dayEnd)) {
                        return Optional.of(prevEnd);
                    }
                    prevEnd = fin.isAfter(prevEnd) ? fin : prevEnd;
                } else {
                    // turno comienza antes que prevEnd, extiende prevEnd si es necesario
                    prevEnd = fin.isAfter(prevEnd) ? fin : prevEnd;
                }
            }

            // después de iterar los turnos del día, comprobar hasta end of day
            long gapEndDay = java.time.Duration.between(prevEnd, dayEnd).toMinutes();
            if (gapEndDay >= durMin && !prevEnd.isAfter(dayEnd)) {
                return Optional.of(prevEnd);
            }

            // avanzar al siguiente día a las 9:00
            cursor = LocalDateTime.of(date.plusDays(1), workStart);
        }

        return Optional.empty();
    }
}
