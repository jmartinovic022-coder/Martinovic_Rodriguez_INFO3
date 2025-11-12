package estructuras.arbol_AVL;

import estructuras.lista_enlazada.ListaEnlazada;

/**
 * Árbol AVL genérico para elementos comparables.
 * 
 * Implementa un árbol binario de búsqueda auto-balanceado para elementos de tipo genérico T.
 * Garantiza operaciones de inserción, eliminación y búsqueda en tiempo O(log n).
 * 
 * Métodos principales:
 * - insertar(T): inserta un elemento manteniendo balance AVL
 * - eliminar(T): elimina un elemento manteniendo balance AVL
 * - mayorOIgual(T key): busca el menor elemento >= key
 * - menorQue(T key): busca el mayor elemento < key
 * - obtenerEnOrden(): retorna lista con recorrido in-orden
 * 
 * No usa colecciones de java.util internamente; devuelve ListaEnlazada para recorridos.
 * 
 * @author Info3 - Estructuras de Datos
 * @version 1.0
 * @param <T> tipo de datos a almacenar (debe implementar Comparable)
 */
public class ArbolAVLGenerico<T extends Comparable<T>> {
    private class Nodo {
        T dato;
        Nodo izq, der;
        int altura;
        Nodo(T d) { dato = d; izq = der = null; altura = 1; }
    }

    private Nodo raiz;

    private int altura(Nodo n) { return (n == null) ? 0 : n.altura; }
    private int balance(Nodo n) { return (n == null) ? 0 : altura(n.izq) - altura(n.der); }

    private Nodo rotDerecha(Nodo y) {
        Nodo x = y.izq;
        Nodo T2 = x.der;
        x.der = y;
        y.izq = T2;
        y.altura = Math.max(altura(y.izq), altura(y.der)) + 1;
        x.altura = Math.max(altura(x.izq), altura(x.der)) + 1;
        return x;
    }

    private Nodo rotIzquierda(Nodo x) {
        Nodo y = x.der;
        Nodo T2 = y.izq;
        y.izq = x;
        x.der = T2;
        x.altura = Math.max(altura(x.izq), altura(x.der)) + 1;
        y.altura = Math.max(altura(y.izq), altura(y.der)) + 1;
        return y;
    }

    /**
     * Inserta un elemento en el árbol manteniendo las propiedades AVL.
     * Si el elemento ya existe, se ignora (sin duplicados).
     * Complejidad: O(log n)
     * 
     * @param dato el elemento a insertar (no null)
     */
    public void insertar(T dato) { 
        raiz = insertRec(raiz, dato); 
    }

    private Nodo insertRec(Nodo nodo, T dato) {
        if (nodo == null) return new Nodo(dato);
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) nodo.izq = insertRec(nodo.izq, dato);
        else if (cmp > 0) nodo.der = insertRec(nodo.der, dato);
        else return nodo; // duplicate
        nodo.altura = 1 + Math.max(altura(nodo.izq), altura(nodo.der));
        int bal = balance(nodo);
        if (bal > 1 && dato.compareTo(nodo.izq.dato) < 0) return rotDerecha(nodo);
        if (bal < -1 && dato.compareTo(nodo.der.dato) > 0) return rotIzquierda(nodo);
        if (bal > 1 && dato.compareTo(nodo.izq.dato) > 0) {
            nodo.izq = rotIzquierda(nodo.izq);
            return rotDerecha(nodo);
        }
        if (bal < -1 && dato.compareTo(nodo.der.dato) < 0) {
            nodo.der = rotDerecha(nodo.der);
            return rotIzquierda(nodo);
        }
        return nodo;
    }

    /**
     * Elimina un elemento del árbol manteniendo las propiedades AVL.
     * Si el elemento no existe, no hace nada.
     * Complejidad: O(log n)
     * 
     * @param dato el elemento a eliminar
     */
    public void eliminar(T dato) { 
        raiz = removeRec(raiz, dato); 
    }

    private Nodo obtenerMinimo(Nodo n) {
        Nodo cur = n; while (cur != null && cur.izq != null) cur = cur.izq; return cur;
    }

    private Nodo removeRec(Nodo nodo, T dato) {
        if (nodo == null) return null;
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) nodo.izq = removeRec(nodo.izq, dato);
        else if (cmp > 0) nodo.der = removeRec(nodo.der, dato);
        else {
            if (nodo.izq == null || nodo.der == null) {
                Nodo temp = (nodo.izq != null) ? nodo.izq : nodo.der;
                if (temp == null) nodo = null; else nodo = temp;
            } else {
                Nodo succ = obtenerMinimo(nodo.der);
                nodo.dato = succ.dato;
                nodo.der = removeRec(nodo.der, succ.dato);
            }
        }
        if (nodo == null) return null;
        nodo.altura = 1 + Math.max(altura(nodo.izq), altura(nodo.der));
        int bal = balance(nodo);
        if (bal > 1 && balance(nodo.izq) >= 0) return rotDerecha(nodo);
        if (bal > 1 && balance(nodo.izq) < 0) { nodo.izq = rotIzquierda(nodo.izq); return rotDerecha(nodo); }
        if (bal < -1 && balance(nodo.der) <= 0) return rotIzquierda(nodo);
        if (bal < -1 && balance(nodo.der) > 0) { nodo.der = rotDerecha(nodo.der); return rotIzquierda(nodo); }
        return nodo;
    }

    /**
     * Busca el menor elemento que es mayor o igual a la clave dada.
     * Utiliza búsqueda binaria balanceada en el árbol.
     * Complejidad: O(log n)
     * 
     * @param key la clave a buscar (límite inferior)
     * @return el menor elemento >= key, o null si no existe
     */
    public T mayorOIgual(T key) {
        Nodo cur = raiz; Nodo candidate = null;
        while (cur != null) {
            int cmp = cur.dato.compareTo(key);
            if (cmp == 0) return cur.dato;
            if (cur.dato.compareTo(key) > 0) { candidate = cur; cur = cur.izq; }
            else cur = cur.der;
        }
        return (candidate == null) ? null : candidate.dato;
    }

    /**
     * Busca el mayor elemento que es menor que la clave dada.
     * Utiliza búsqueda binaria balanceada en el árbol.
     * Complejidad: O(log n)
     * 
     * @param key la clave a buscar (límite superior)
     * @return el mayor elemento < key, o null si no existe
     */
    public T menorQue(T key) {
        Nodo cur = raiz; Nodo candidate = null;
        while (cur != null) {
            if (cur.dato.compareTo(key) < 0) { candidate = cur; cur = cur.der; }
            else cur = cur.izq;
        }
        return (candidate == null) ? null : candidate.dato;
    }

    /**
     * Obtiene todos los elementos del árbol en orden in-orden (izq-raíz-der).
     * Complejidad: O(n) para recorrer n elementos.
     * 
     * @return ListaEnlazada con todos los elementos ordenados
     */
    public ListaEnlazada<T> obtenerEnOrden() {
        ListaEnlazada<T> out = new ListaEnlazada<>();
        recorridoInOrdenRec(raiz, out);
        return out;
    }

    private void recorridoInOrdenRec(Nodo n, ListaEnlazada<T> out) {
        if (n == null) return;
        recorridoInOrdenRec(n.izq, out);
        out.insertarFinal(n.dato);
        recorridoInOrdenRec(n.der, out);
    }
}
