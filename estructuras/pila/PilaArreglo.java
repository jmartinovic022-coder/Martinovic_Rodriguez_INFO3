package estructuras.pila;

/**
 * Implementación simple de una pila basada en arreglo (genérica).
 *
 * Soporta operaciones típicas LIFO: apilar, desapilar y consulta de cima.
 * Además proporciona utilidades para mostrar el contenido y un método
 * auxiliar que escribe una palabra invertida extrayendo caracteres.
 *
 * @param <T> tipo de los elementos almacenados en la pila
 */
public class PilaArreglo<T> {
    private T[] arreglo;   // almacena los elementos
    private int tope;        // índice del último elemento
    private int capacidad;   // tamaño máximo de la pila

    /**
     * Crea una pila con la capacidad indicada.
     *
     * @param capacidad capacidad máxima de la pila
     */
    @SuppressWarnings("unchecked")
    public PilaArreglo(int capacidad) {
        this.capacidad = capacidad;
        this.arreglo = (T[]) new Object[capacidad];
        this.tope = -1; // la pila empieza vacía
    }

    /**
     * Agrega un elemento a la pila. Si está llena imprime un mensaje de error
     * y no realiza la inserción.
     *
     * @param dato elemento a apilar
     */
    public void apilar(T dato) {
        if (estaLlena()) {
            System.out.println("Error: la pila está llena, no se puede agregar " + dato);
        } else {
            arreglo[++tope] = dato; // incrementa y asigna
        }
    }

    /**
     * Extrae y devuelve el elemento en la cima de la pila. Si la pila está
     * vacía devuelve null e imprime un mensaje de error.
     *
     * @return el elemento desapilado o null si la pila está vacía
     */
    public T desapilar() {
        if (estaVacia()) {
            System.out.println("Error: la pila está vacía");
            return null; // valor por defecto
        } else {
            return arreglo[tope--]; // devuelve y decrementa
        }
    }

    /**
     * Devuelve el elemento en la cima sin eliminarlo. Si la pila está vacía
     * devuelve null e imprime un mensaje.
     *
     * @return el elemento en la cima o null si la pila está vacía
     */
    public T cima() {
        if (estaVacia()) {
            System.out.println("Error: la pila está vacía");
            return null;
        } else {
            return arreglo[tope];
        }
    }

    /**
     * Indica si la pila está vacía.
     *
     * @return true si no hay elementos en la pila
     */
    public boolean estaVacia() {
        return tope == -1;
    }

    /**
     * Indica si la pila está llena.
     *
     * @return true si la pila alcanzó su capacidad máxima
     */
    public boolean estaLlena() {
        return tope == capacidad - 1;
    }

    /**
     * Imprime el contenido de la pila de abajo hacia arriba (desde la base
     * hasta la cima) para propósitos de depuración o demostración.
     */
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("Pila: vacía");
        } else {
            System.out.print("Pila: ");
            for (int i = 0; i <= tope; i++) {
                System.out.print(arreglo[i] + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Escribe por consola una palabra invertida desapilando sus caracteres.
     * Método auxiliar utilizado en ejercicios didácticos.
     */
    public void escribirPalabraInvertida() {
        System.out.print("Palabra invertida: ");
        while(!estaVacia()) {
            System.out.print(desapilar());
        }
        System.out.println();
    }
}
