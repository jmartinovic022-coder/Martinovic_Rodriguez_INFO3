package estructuras.cola;

/**
 * Cola circular basada en arreglo (implementación genérica).
 *
 * Implementa una cola de tamaño fijo con comportamiento FIFO usando un
 * buffer circular. Métodos públicos proporcionan operaciones típicas:
 * encolar, desencolar, verFrente y comprobaciones de estado.
 *
 * @param <T> tipo de los elementos almacenados
 */
public class ColaArreglo<T> {
    // ...campos internos
    private T[] arreglo;   // almacena los elementos
    private int frente;    // índice del primer elemento
    private int fin;       // índice del último elemento
    private int cantidad;  // cantidad de elementos actuales
    private int capacidad; // tamaño máximo de la cola

    // Constructor
    @SuppressWarnings("unchecked")
    public ColaArreglo(int capacidad) {
        this.capacidad = capacidad;
        this.arreglo = (T[]) new Object[capacidad]; // cast necesario
        this.frente = 0;
        this.fin = -1;
        this.cantidad = 0;
    }

    /**
     * Agrega un elemento al final de la cola.
     * Si la cola está llena sólo imprime un mensaje de error y no encola.
     *
     * @param dato elemento a encolar
     */
    public void encolar(T dato) {
        if (estaLlena()) {
            System.out.println("Error: la cola está llena, no se puede agregar " + dato);
        } else {
            fin = (fin + 1) % capacidad;
            arreglo[fin] = dato;
            cantidad++;
        }
    }

    /**
     * Elimina y devuelve el primer elemento de la cola.
     * Si la cola está vacía devuelve null y muestra un mensaje de error.
     *
     * @return el elemento desencolado o null si la cola está vacía
     */
    public T desencolar() {
        if (estaVacia()) {
            System.out.println("Error: la cola está vacía");
            return null;
        } else {
            T valor = arreglo[frente];
            arreglo[frente] = null;
            frente = (frente + 1) % capacidad;
            cantidad--;
            return valor;
        }
    }

    /**
     * Devuelve (sin eliminar) el elemento al frente de la cola.
     * Si la cola está vacía devuelve null y muestra un mensaje.
     *
     * @return el elemento en el frente o null si la cola está vacía
     */
    public T verFrente() {
        if (estaVacia()) {
            System.out.println("Error: la cola está vacía");
            return null;
        } else {
            return arreglo[frente];
        }
    }

    /**
     * Indica si la cola está vacía.
     *
     * @return true si no hay elementos, false en caso contrario
     */
    public boolean estaVacia() {
        return cantidad == 0;
    }

    /**
     * Indica si la cola está llena (se alcanzó la capacidad máxima).
     *
     * @return true si la cola está llena, false en caso contrario
     */
    public boolean estaLlena() {
        return cantidad == capacidad;
    }

    /**
     * Imprime por consola el contenido de la cola en orden desde el frente
     * hasta el final. Utiliza la porción lógica (cantidad) del buffer circular.
     */
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("Cola: vacía");
        } else {
            System.out.print("Cola: ");
            for (int i = 0; i < cantidad; i++) {
                int indice = (frente + i) % capacidad;
                System.out.print(arreglo[indice] + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Devuelve la cantidad actual de elementos almacenados en la cola.
     *
     * @return número de elementos en la cola
     */
    public int contar() {
        return cantidad;
    }
}
