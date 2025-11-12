package estructuras.monticulo_binario;

import java.util.Arrays;

/**
 * Montículo mínimo genérico (Min-Heap) implementado sobre un arreglo de
 * {@code Object} con casteos a {@code T}. Está pensado con fines didácticos
 * y ofrece operaciones básicas: insertar, extraer el mínimo y consulta de
 * estado. No usa colecciones externas para mantener la implementación
 * simple y clara.
 *
 * @param <T> tipo de elementos (debe implementar {@link Comparable})
 */
public class MinHeapGenerico<T extends Comparable<T>> {
    private Object[] heap;
    private int size;

    public MinHeapGenerico() {
        this.heap = new Object[10];
        this.size = 0;
    }

    /**
     * Dobla la capacidad interna si es necesario.
     */
    private void asegurarCapacidad() {
        if (size >= heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }

    private int padre(int i) { return (i - 1) / 2; }
    private int izquierdo(int i) { return 2 * i + 1; }
    private int derecho(int i) { return 2 * i + 2; }

    /**
     * Intercambia dos posiciones del arreglo interno.
     */
    @SuppressWarnings("unchecked")
    private void intercambiar(int i, int j) {
        Object tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    /**
     * Inserta un valor en el montículo manteniendo la propiedad de min-heap.
     *
     * @param val valor a insertar
     */
    @SuppressWarnings("unchecked")
    public void insertar(T val) {
        asegurarCapacidad();
        heap[size++] = val;
        int idx = size - 1;
        while (idx > 0) {
            int p = padre(idx);
            T cur = (T) heap[idx];
            T par = (T) heap[p];
            if (cur.compareTo(par) < 0) {
                intercambiar(idx, p);
                idx = p;
            } else break;
        }
    }

    /**
     * Extrae y devuelve el elemento mínimo (raíz) del montículo. Devuelve
     * {@code null} si el montículo está vacío.
     *
     * @return el elemento mínimo o null si no hay elementos
     */
    @SuppressWarnings("unchecked")
    public T extraerMin() {
        if (estaVacio()) return null;
        T min = (T) heap[0];
        if (size == 1) {
            heap[0] = null;
            size = 0;
            return min;
        }
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        int idx = 0;
        while (true) {
            int l = izquierdo(idx);
            int r = derecho(idx);
            int smallest = idx;
            if (l < size && ((T) heap[l]).compareTo((T) heap[smallest]) < 0) smallest = l;
            if (r < size && ((T) heap[r]).compareTo((T) heap[smallest]) < 0) smallest = r;
            if (smallest != idx) {
                intercambiar(idx, smallest);
                idx = smallest;
            } else break;
        }
        return min;
    }

    /**
     * Devuelve el número de elementos almacenados en el montículo.
     *
     * @return cantidad de elementos
     */
    public int contar() { return size; }

    /**
     * Indica si el montículo está vacío.
     *
     * @return true si no contiene elementos
     */
    public boolean estaVacio() { return size == 0; }

    /**
     * Devuelve una representación en forma de arreglo (porción usada) del
     * montículo para propósitos de depuración.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
