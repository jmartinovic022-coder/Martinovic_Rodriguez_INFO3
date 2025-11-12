package estructuras.mapa;

import java.util.ArrayList;
import java.util.List;

/**
 * HashMapPersonalizado: implementación didáctica de un mapa hash que usa
 * encadenamiento (listas enlazadas) para resolver colisiones. No pretende
 * reemplazar java.util.HashMap, pero sirve para entender la lógica básica
 * (hash, buckets, rehashing).
 *
 * @param <K> tipo de claves (se espera que implemente {@link Object#hashCode})
 * @param <V> tipo de valores
 */
public class HashMapPersonalizado<K, V> {
    /**
     * Entrada almacenada en cada bucket: par clave-valor y puntero al siguiente
     * en la lista (encadenamiento).
     */
    private static class Entry<K, V> {
        K clave;
        V valor;
        Entry<K, V> siguiente;

        Entry(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Entry<K, V>[] tabla;
    private int cantidad;
    private final double factorCarga = 0.75;

    /**
     * Crea un HashMap personalizado con la capacidad inicial indicada.
     *
     * @param capacidadInicial número de buckets inicial
     */
    @SuppressWarnings("unchecked")
    public HashMapPersonalizado(int capacidadInicial) {
        this.tabla = (Entry<K, V>[]) new Entry[capacidadInicial];
        this.cantidad = 0;
    }

    /**
     * Devuelve el número de entradas almacenadas.
     *
     * @return cantidad de pares clave-valor
     */
    public int contar() {
        return this.cantidad;
    }

    /**
     * Inserta o actualiza el valor asociado a la clave dada.
     * Si la clave existe, su valor se reemplaza; si no, se añade una nueva entrada
     * al inicio de la lista del bucket correspondiente.
     *
     * @param clave clave a insertar/actualizar
     * @param valor valor asociado
     */
    public void poner(K clave, V valor) {
        int indice = Math.abs(clave.hashCode()) % tabla.length;
        Entry<K, V> cabeza = tabla[indice];
        Entry<K, V> actual = cabeza;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }
        Entry<K, V> nuevo = new Entry<>(clave, valor);
        nuevo.siguiente = cabeza;
        tabla[indice] = nuevo;
        cantidad++;
        if ((double) cantidad / tabla.length > factorCarga) {
            rehash();
        }
    }

    /**
     * Recupera el valor asociado a la clave, o null si no existe.
     *
     * @param clave clave a buscar
     * @return el valor asociado o null si no se encontró
     */
    public V obtener(K clave) {
        int indice = Math.abs(clave.hashCode()) % tabla.length;
        Entry<K, V> actual = tabla[indice];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Elimina la entrada asociada a la clave indicada.
     *
     * @param clave clave cuya entrada se desea eliminar
     * @return true si se eliminó una entrada, false si no existía
     */
    public boolean eliminar(K clave) {
        int indice = Math.abs(clave.hashCode()) % tabla.length;
        Entry<K, V> actual = tabla[indice];
        Entry<K, V> anterior = null;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                cantidad--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Devuelve una lista con todos los valores actualmente almacenados en el mapa.
     *
     * @return lista de valores
     */
    public List<V> valores() {
        List<V> out = new ArrayList<>();
        for (Entry<K, V> bucket : tabla) {
            Entry<K, V> actual = bucket;
            while (actual != null) {
                out.add(actual.valor);
                actual = actual.siguiente;
            }
        }
        return out;
    }

    /**
     * Redimensiona la tabla (dobla la cantidad de buckets) y re-inserta todas
     * las entradas. Método interno invocado cuando se supera el factor de carga.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Entry<K, V>[] viejaTabla = tabla;
        tabla = (Entry<K, V>[]) new Entry[viejaTabla.length * 2];
        int viejaCantidad = cantidad;
        cantidad = 0;
        for (Entry<K, V> bucket : viejaTabla) {
            Entry<K, V> actual = bucket;
            while (actual != null) {
                poner(actual.clave, actual.valor);
                actual = actual.siguiente;
            }
        }
    }
}
