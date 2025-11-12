
package practicos.practico_09_integrador.src;
import java.time.LocalDateTime;
import java.util.Optional;
import estructuras.lista_enlazada.ListaEnlazada;

public interface AgendaMedico {
    boolean agendar(Turno t);
    boolean cancelar(String idTurno);
    Optional<Turno> siguiente(LocalDateTime t);
    ListaEnlazada<Turno> getTurnosOrdenados();
}
