
package practicos.practico_09_integrador.src;
import java.time.LocalDateTime;
import estructuras.monticulo_binario.MinHeapGenerico;
import estructuras.mapa.HashMapPersonalizado;

public class PlannerRecordatorios implements Planner {
    private final MinHeapGenerico<Recordatorio> heap;
    private final HashMapPersonalizado<String, Recordatorio> byId;

    public PlannerRecordatorios() {
        this.heap = new MinHeapGenerico<>();
        this.byId = new HashMapPersonalizado<>(16);
    }

    @Override
    public void programar(Recordatorio r) {
        if (r == null) return;
        heap.insertar(r);
        byId.poner(r.getId(), r);
    }

    @Override
    public Recordatorio proximo() {
        Recordatorio r = heap.extraerMin();
        if (r != null) {
            byId.eliminar(r.getId());
        }
        return r;
    }

    @Override
    public boolean reprogramar(String id, LocalDateTime nuevaFecha) {
        if (id == null || nuevaFecha == null) return false;
        Recordatorio r = byId.obtener(id);
        if (r == null) return false;
        // Remove from map and update
        byId.eliminar(id);
        r.setFecha(nuevaFecha);
        // Clear heap and rebuild by re-inserting all recordatorios from byId
        while (!heap.estaVacio()) {
            heap.extraerMin();
        }
        // Get all values and rebuild heap
        for (Recordatorio rec : byId.valores()) {
            heap.insertar(rec);
        }
        // Also insert the updated recordatorio
        heap.insertar(r);
        // Update the map with the modified recordatorio
        byId.poner(r.getId(), r);
        return true;
    }
}
