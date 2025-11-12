package estructuras.pila;

/**
 * Pila dinámica sin usar colecciones estándar.
 * Implementada con arreglo de Object y resize automático.
 */
public class PilaDinamica<T> {
    private Object[] arr;
    private int top;

    public PilaDinamica() {
        this.arr = new Object[10];
        this.top = -1;
    }

    private void asegurarCapacidad() {
        if (top + 1 >= arr.length) {
            Object[] n = new Object[arr.length * 2];
            System.arraycopy(arr, 0, n, 0, arr.length);
            arr = n;
        }
    }

    public void apilar(T dato) {
        asegurarCapacidad();
        arr[++top] = dato;
    }

    @SuppressWarnings("unchecked")
    public T desapilar() {
        if (estaVacia()) {
            return null;
        }
        T val = (T) arr[top];
        arr[top--] = null;
        return val;
    }

    @SuppressWarnings("unchecked")
    public T cima() {
        if (estaVacia()) return null;
        return (T) arr[top];
    }

    public boolean estaVacia() { return top == -1; }

    public int contar() { return top + 1; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i <= top; i++) {
            sb.append(arr[i]);
            if (i < top) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
