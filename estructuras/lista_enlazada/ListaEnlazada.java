package estructuras.lista_enlazada;

import java.util.Iterator;

/**
 * Implementación simple de una lista enlazada simple (singly linked list).
 * Proporciona operaciones habituales: inserción al inicio/fin, inserción en
 * posición, eliminación por valor o por índice, búsqueda, inversión y
 * eliminación de duplicados. Implementa {@link Iterable} para soportar
 * bucles for-each.
 *
 * @param <T> tipo de los elementos almacenados
 */
public class ListaEnlazada<T> implements Iterable<T> {
    private Nodo<T> cabeza; // primer nodo de la lista



    /**
     * Crea una lista vacía.
     */
    public ListaEnlazada() {
        this.cabeza = null;
    }



    /**
     * Inserta un elemento al inicio de la lista.
     *
     * @param dato elemento a insertar
     */
    public void insertarInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
    }

    /**
     * Inserta un elemento al final de la lista.
     *
     * @param dato elemento a insertar
     */
    public void insertarFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> aux = cabeza;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
    }

    /**
     * Elimina la primera ocurrencia del valor indicado (si existe).
     *
     * @param valor valor a eliminar
     */
    public void eliminar(T valor) {
        if (cabeza == null) return;

        if (cabeza.getDato().equals(valor)) {
            cabeza = cabeza.getSiguiente();
            return;
        }

        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null && 
               !actual.getSiguiente().getDato().equals(valor)) {
            actual = actual.getSiguiente();
        }

        if (actual.getSiguiente() != null) {
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }
    }

    /**
     * Elimina el elemento en la posición indicada (0 = inicio). Si la posición
     * es inválida imprime un mensaje y no realiza cambios.
     *
     * @param pos índice del elemento a eliminar
     */
    public void eliminarEn(int pos) {
        int n = contar();

        if (pos < 0 || pos >= n) {
            System.out.println("Posición inválida. Ingrese un valor entre 0 y " + (n - 1));
            return;
        }

        if (pos == 0) {
            cabeza = cabeza.getSiguiente();
            return;
        }

        Nodo<T> aux = cabeza;
        int i = 0;
        while (aux != null && i < pos - 1) {
            aux = aux.getSiguiente();
            i++;
        }

        if (aux != null && aux.getSiguiente() != null) {
            aux.setSiguiente(aux.getSiguiente().getSiguiente());
        }
    }

    /**
     * Reemplaza el elemento en la posición indicada por `dato`.
     *
     * @param pos índice donde setear el nuevo valor
     * @param dato nuevo valor a almacenar
     */
    public void setEn(int pos, T dato) {
        int n = contar();

        if (pos < 0 || pos >= n) {
            System.out.println("Posición inválida. Ingrese un valor entre 0 y " + (n - 1));
            return;
        }

        Nodo<T> aux = cabeza;
        int i = 0;
        while (aux != null && i < pos) {
            aux = aux.getSiguiente();
            i++;
        }

        if (aux != null) {
            aux.setDato(dato);
        }
    }

    /**
     * Busca si la lista contiene el valor dado.
     *
     * @param valor valor a buscar
     * @return true si existe al menos una coincidencia
     */
    public boolean buscar(T valor) {
        Nodo<T> aux = cabeza;
        while (aux != null) {
            if (aux.getDato().equals(valor)) return true;
            aux = aux.getSiguiente();
        }
        return false;
    }

    /**
     * Cuenta el número de elementos de la lista.
     *
     * @return número de nodos en la lista
     */
    public int contar() {
        int count = 0;
        Nodo<T> aux = cabeza;
        while (aux != null) {
            count++;
            aux = aux.getSiguiente();
        }
        return count;
    }

    /**
     * Invierte la lista in-place.
     */
    public void invertir() {
        Nodo<T> prev = null;
        Nodo<T> actual = cabeza;
        Nodo<T> siguiente;

        while (actual != null) {
            siguiente = actual.getSiguiente();
            actual.setSiguiente(prev);
            prev = actual;
            actual = siguiente;
        }
        cabeza = prev;
    }

    /**
     * Inserta un elemento en la posición indicada (0 = inicio). Si la
     * posición es inválida imprime un mensaje y no realiza la inserción.
     *
     * @param pos posición donde insertar
     * @param dato elemento a insertar
     */
    public void insertarEn(int pos, T dato) {
        int n = contar();

        if (pos < 0 || pos > n) {
            System.out.println("Posición inválida. Ingrese un valor entre 0 y " + n);
            return;
        }

        if (pos == 0) {
            insertarInicio(dato);
            return;
        }

        Nodo<T> nuevo = new Nodo<>(dato);
        Nodo<T> aux = cabeza;
        int i = 0;

        while (aux != null && i < pos - 1) {
            aux = aux.getSiguiente();
            i++;
        }

        nuevo.setSiguiente(aux.getSiguiente());
        aux.setSiguiente(nuevo);
    }


    /**
     * Elimina elementos duplicados conservando la primera aparición de cada
     * valor.
     */
    public void eliminarDuplicados() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            Nodo<T> runner = actual;
            while (runner.getSiguiente() != null) {
                if (actual.getDato().equals(runner.getSiguiente().getDato())) {
                    runner.setSiguiente(runner.getSiguiente().getSiguiente());
                } else {
                    runner = runner.getSiguiente();
                }
            }
            actual = actual.getSiguiente();
        }
    }

    /**
     * Obtiene el elemento en la posición indicada.
     *
     * @param pos índice del elemento a obtener
     * @return el elemento o null si la posición es inválida
     */
    public T obtener(int pos) {
        int n = contar();

        if (pos < 0 || pos >= n) {
            System.out.println("Posición inválida. Ingrese un valor entre 0 y " + (n - 1));
            return null;
        }

        Nodo<T> aux = cabeza;
        int i = 0;

        while (aux != null && i < pos) {
            aux = aux.getSiguiente();
            i++;
        }

        return aux != null ? aux.getDato() : null;
    }

    /**
     * Imprime la lista por consola en formato: a -> b -> c -> null
     */
    public void imprimir() {
        Nodo<T> aux = cabeza;
        while (aux != null) {
            System.out.print(aux.getDato() + " -> ");
            aux = aux.getSiguiente();
        }
        System.out.println("null");
    }

    // Implement Iterable to allow for-each loops
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> current = cabeza;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T val = current.getDato();
                current = current.getSiguiente();
                return val;
            }
        };
    }
}
