package estructuras.cola;

/**
 * ColaCircular: implementación de una cola circular genérica.
 * Si la cola está llena, sobrescribe el elemento más antiguo.
 */
public class ColaCircular<T> {
    private T[] array;
    private final int capacidad;
    private int frente = 0;
    private int fin = 0;
    private int cantidad = 0;

    @SuppressWarnings("unchecked")
    public ColaCircular(int capacidad) {
        if (capacidad <= 0) throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        this.capacidad = capacidad;
        this.array = (T[]) new Object[capacidad];
    }

    public int contar() {
        return cantidad;
    }

    public boolean estaVacia() {
        return cantidad == 0;
    }

    public boolean estaLlena() {
        return cantidad == capacidad;
    }

    public T verFrente() {
        if (estaVacia()) return null;
        return array[frente];
    }

    /**
     * Llega un nuevo dato a la cola.
     * Si la cola está llena, pisa al más antiguo (sobrescribe y avanza frente y fin, cantidad no cambia).
     * Si no está llena, añade al fin y aumenta cantidad.
     */
    public void encolar(T dato) {
        if (estaLlena()) {
            array[fin] = dato;
            fin = (fin + 1) % capacidad;
            frente = (frente + 1) % capacidad;
        } else {
            array[fin] = dato;
            fin = (fin + 1) % capacidad;
            cantidad++;
        }
    }

    /**
     * Atiende (desencolar): retorna null si está vacía, sino retorna el elemento en frente y actualiza punteros.
     */
    public T desencolar() {
        if (estaVacia()) return null;
        T dato = array[frente];
        array[frente] = null;
        frente = (frente + 1) % capacidad;
        cantidad--;
        return dato;
    }

    /**
     * Indica si la cola contiene el elemento dado (usa equals).
     */
    public boolean contiene(T dato) {
        if (dato == null) return false;
        for (int i = 0, idx = frente; i < cantidad; i++, idx = (idx + 1) % capacidad) {
            T v = array[idx];
            if (dato.equals(v)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ColaCircular[");
        for (int i = 0, idx = frente; i < cantidad; i++, idx = (idx + 1) % capacidad) {
            if (i > 0) sb.append(", ");
            sb.append(array[idx]);
        }
        sb.append("]");
        return sb.toString();
    }
}
