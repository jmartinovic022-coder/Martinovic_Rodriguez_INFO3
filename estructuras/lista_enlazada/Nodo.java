package estructuras.lista_enlazada;

/**
 * Nodo simple utilizado por {@link ListaEnlazada}.
 *
 * @param <T> tipo del dato almacenado
 */
public class Nodo<T> {
    T dato;            // valor genérico
    Nodo<T> siguiente; // referencia al próximo nodo

    /**
     * Crea un nodo con el dato provisto y siguiente nulo.
     *
     * @param dato valor a almacenar en el nodo
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    // Getters y Setters
    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
