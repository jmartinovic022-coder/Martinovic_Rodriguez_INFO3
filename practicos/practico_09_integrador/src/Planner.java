
package practicos.practico_09_integrador.src;
import java.time.LocalDateTime;

public interface Planner {
    void programar(Recordatorio r);
    Recordatorio proximo();
    boolean reprogramar(String id, LocalDateTime nuevaFecha);
}
