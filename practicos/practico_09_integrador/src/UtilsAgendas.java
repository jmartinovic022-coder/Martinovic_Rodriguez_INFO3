
package practicos.practico_09_integrador.src;

import estructuras.lista_enlazada.ListaEnlazada;

/**
 * Utilidades para agendas.
 */
public class UtilsAgendas {

    /**
     * Merge de dos listas ordenadas de Turno (por fecha). Retorna una nueva lista ordenada C.
     * Si un turno aparece en ambas listas (mismo id o mismo medico y misma fechaHora) se añade solo una
     * instancia y se registra el conflicto en stderr.
     */
    public static ListaEnlazada<Turno> merge(ListaEnlazada<Turno> A, ListaEnlazada<Turno> B) {
        ListaEnlazada<Turno> C = new ListaEnlazada<>();
        if (A == null) A = new ListaEnlazada<>();
        if (B == null) B = new ListaEnlazada<>();

        java.util.Iterator<Turno> itA = A.iterator();
        java.util.Iterator<Turno> itB = B.iterator();
        Turno a = itA.hasNext() ? itA.next() : null;
        Turno b = itB.hasNext() ? itB.next() : null;

        while (a != null && b != null) {

            int cmp = a.compareTo(b);
            if (cmp < 0) {
                C.insertarFinal(a);
                a = itA.hasNext() ? itA.next() : null;
            } else if (cmp > 0) {
                C.insertarFinal(b);
                b = itB.hasNext() ? itB.next() : null;
            } else {
                if (isSameTurno(a, b)) {
                    System.err.println("Conflicto: turno duplicado id=" + a.getId() + " (misma fecha/medico)");
                    C.insertarFinal(a);
                } else if (a.getId() != null && a.getId().equals(b.getId())) {
                    System.err.println("Conflicto: mismo id en A y B id=" + a.getId());
                    C.insertarFinal(a);
                } else {
                    C.insertarFinal(a);
                    C.insertarFinal(b);
                }
                a = itA.hasNext() ? itA.next() : null;
                b = itB.hasNext() ? itB.next() : null;
            }
        }

        while (a != null) {
            C.insertarFinal(a);
            a = itA.hasNext() ? itA.next() : null;
        }
        while (b != null) {
            C.insertarFinal(b);
            b = itB.hasNext() ? itB.next() : null;
        }

        // Deduplicación final por id (sin usar colecciones java.util)
        ListaEnlazada<Turno> finalList = new ListaEnlazada<>();
        for (Turno t : C) {
            if (t == null) continue;
            String id = t.getId();
            boolean seen = false;
            if (id != null) {
                for (Turno existing : finalList) {
                    if (existing != null && id.equals(existing.getId())) { seen = true; break; }
                }
                if (seen) {
                    System.err.println("Conflicto: turno con id repetido, ignorando id=" + id);
                    continue;
                }
            }
            finalList.insertarFinal(t);
        }

        return finalList;
    }

    private static boolean isSameTurno(Turno a, Turno b) {
        if (a == null || b == null) return false;
        if (a.getMatriculaMedico() == null || b.getMatriculaMedico() == null) return false;
        if (!a.getMatriculaMedico().equals(b.getMatriculaMedico())) return false;
        if (a.getFechaHora() == null || b.getFechaHora() == null) return false;
        return a.getFechaHora().equals(b.getFechaHora());
    }
}
