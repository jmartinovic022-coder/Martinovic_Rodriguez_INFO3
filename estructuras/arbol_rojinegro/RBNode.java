package estructuras.arbol_rojinegro;

/**
 * Nodo genérico para un árbol rojo-negro.
 * 
 * Almacena pares clave-valor con información de color (rojo o negro) requerida
 * por el balanceo de árbol rojo-negro. Cada nodo mantiene referencias a sus hijos
 * (left, right) y a su padre (parent) para permitir navegación en múltiples direcciones.
 * 
 * @author Info3 - Estructuras de Datos
 * @version 1.0
 * @param <K> tipo de clave (debe ser comparable)
 * @param <V> tipo de valor asociado
 */
public class RBNode<K extends Comparable<K>, V> {
    /**
     * Enumeración de colores posibles para los nodos del árbol rojo-negro.
     * ROJO: usado durante inserción y requiere rebalanceo.
     * NEGRO: color final tras rebalanceo.
     */
    public enum Color { ROJO, NEGRO }

    /** Clave del nodo (inmutable una vez insertado) */
    public K key;
    
    /** Valor asociado a la clave */
    public V val;
    
    /** Color del nodo: ROJO o NEGRO */
    public Color color;
    
    /** Referencia al hijo izquierdo (valores menores) */
    public RBNode<K,V> left;
    
    /** Referencia al hijo derecho (valores mayores) */
    public RBNode<K,V> right;
    
    /** Referencia al nodo padre */
    public RBNode<K,V> parent;

    /**
     * Constructor general que inicializa un nodo RB.
     * 
     * Si nil no es null, los campos left, right, parent se inicializan a nil.
     * Esto es útil para vincular con el centinela NIL del árbol.
     * 
     * @param key la clave a almacenar
     * @param val el valor asociado
     * @param color el color inicial del nodo (típicamente ROJO para insertados nuevos)
     * @param nil el nodo centinela NIL (puede ser null si no hay centinela disponible)
     */
    public RBNode(K key, V val, Color color, RBNode<K,V> nil) {
        this.key = key;
        this.val = val;
        this.color = color;
        if (nil != null) {
            this.left = nil;
            this.right = nil;
            this.parent = nil;
        } else {
            this.left = this.right = this.parent = null;
        }
    }

    /**
     * Constructor alternativo para crear nodos sin pasar un centinela NIL.
     * Los campos left, right, parent se inicializan a null.
     * Útil para crear nodos aislados o el nodo NIL en sí mismo.
     * 
     * @param key la clave a almacenar
     * @param val el valor asociado
     * @param color el color del nodo
     */
    public RBNode(K key, V val, Color color) {
        this(key, val, color, null);
    }

    /**
     * Representación en string del nodo para debugging.
     * Formato: (clave:valor,color) donde color es R (rojo) o N (negro).
     * 
     * @return string representativo del nodo
     */
    @Override
    public String toString() {
        return String.format("(%s:%s,%s)", key, val, color == Color.ROJO ? "R" : "N");
    }
}
