package estructuras.arbol_rojinegro;

import estructuras.lista_enlazada.ListaEnlazada;

/**
 * Árbol Rojo-Negro (Red-Black Tree) genérico.
 * 
 * Implementa un árbol binario de búsqueda auto-balanceado que mantiene propiedades RB:
 * - Raíz siempre negra
 * - Si un nodo es rojo, sus hijos son negros
 * - Todos los caminos a hojas tienen igual cantidad de nodos negros
 * 
 * Garantiza O(log n) para insert, delete, search.
 * 
 * @author Info3 - Estructuras de Datos
 * @version 1.0
 * @param <K> tipo de clave (comparable)
 * @param <V> tipo de valor
 */
public class RBTree<K extends Comparable<K>, V> {
    // Ejercicio 1: Nodo y NIL sentinel
    /** Nodo centinela NIL (negro, apunta a sí mismo) */
    public final RBNode<K,V> NIL;
    
    /** Raíz del árbol (siempre negra) */
    public RBNode<K,V> root;

    /**
     * Inicializa un árbol rojo-negro vacío.
     * Crea el centinela NIL que apunta a sí mismo y establece root = NIL.
     */
    public RBTree() {
        // Crear NIL único y sus punteros a sí mismo
        NIL = new RBNode<>(null, null, RBNode.Color.NEGRO);
        NIL.left = NIL.right = NIL.parent = NIL;
        root = NIL; // raíz = NIL
    }

    /**
     * Rotación izquierda: sube el hijo derecho (y) y baja el nodo (x).
     * Actualiza root si x es la raíz.
     * @param x el nodo a rotar
     */
    public void rotIzquierda(RBNode<K,V> x) {
        if (x == NIL) return;
        RBNode<K,V> y = x.right;
        if (y == NIL) return; // no es posible
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    /**
     * Rotación derecha: sube el hijo izquierdo (x) y baja el nodo (y).
     * Simétrica a rotateLeft.
     * @param y el nodo a rotar
     */
    public void rotDerecha(RBNode<K,V> y) {
        if (y == NIL) return;
        RBNode<K,V> x = y.left;
        if (x == NIL) return;
        y.left = x.right;
        if (x.right != NIL) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == NIL) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }

    /**
     * Inserta como árbol binario de búsqueda (sin balanceo).
     * El nuevo nodo es ROJO. Requiere fixInsert después.
     * @param key la clave
     * @param val el valor
     * @return el nodo recién insertado
     */
    public RBNode<K,V> insertarABB(K key, V val) {
        RBNode<K,V> z = new RBNode<>(key, val, RBNode.Color.ROJO, NIL); // hijos/padre = NIL
        RBNode<K,V> y = NIL;
        RBNode<K,V> x = root;
        while (x != NIL) {
            y = x;
            if (z.key.compareTo(x.key) < 0) x = x.left;
            else x = x.right;
        }
        z.parent = y;
        if (y == NIL) root = z;
        else if (z.key.compareTo(y.key) < 0) y.left = z;
        else y.right = z;
        
        // left/right ya apuntan a NIL por constructor
        return z;
    }

    /**
     * Helpers para fixInsert (clasificador)
     * EJERCICIO 5: Clasificador de caso para fixInsert.
     * Devuelve un valor de la enumeración {@link Caso} indicando el tipo de
     * configuración relativa entre nodo, padre y abuelo (por ejemplo LL, LR, etc.).
     *
     * @return un {@link Caso} que describe la situación para la operación de
     *         reparación (fixInsert). NONE indica que no hay violación.
     */
    public enum Caso { TIO_ROJO, LL, RR, LR, RL, NONE }

    /**
     * Analiza la posición de z respecto a su padre y abuelo y determina el caso
     * que debe resolver {@link #arreglarInsercion(RBNode)}.
     *
     * @param z el nodo recientemente insertado (posible violador rojo-rojo)
     * @return el {@link Caso} detectado (TIO_ROJO, LL, LR, RR, RL o NONE)
     */
    public Caso clasificarCaso(RBNode<K,V> z) {
        if (z == NIL || z.parent == NIL || z.parent.parent == NIL) return Caso.NONE;
        
        // Solo clasificamos si hay violación rojo-rojo
        if (z.color != RBNode.Color.ROJO || z.parent.color != RBNode.Color.ROJO) {
            return Caso.NONE;
        }
        
        RBNode<K,V> p = z.parent;
        RBNode<K,V> g = p.parent;
        RBNode<K,V> tio = (p == g.left) ? g.right : g.left;
        
        System.out.println("\nAnálisis de caso:");
        System.out.println("- Nodo z: " + z.key + " (color: " + (z.color == RBNode.Color.ROJO ? "ROJO" : "NEGRO") + ")");
        System.out.println("- Padre: " + p.key + " (color: " + (p.color == RBNode.Color.ROJO ? "ROJO" : "NEGRO") + ")");
        System.out.println("- Abuelo: " + g.key + " (color: " + (g.color == RBNode.Color.ROJO ? "ROJO" : "NEGRO") + ")");
        System.out.println("- Tío: " + (tio == NIL ? "NIL" : tio.key + " (color: " + (tio.color == RBNode.Color.ROJO ? "ROJO" : "NEGRO") + ")"));
        
        if (tio != NIL && tio.color == RBNode.Color.ROJO) {
            System.out.println("=> Caso TIO_ROJO: padre y tío son rojos");
            return Caso.TIO_ROJO;
        }
        
        // si tío es negro o NIL, decidir entre LL, LR, RR, RL
        Caso caso;
        if (p == g.left) {
            if (z == p.left) caso = Caso.LL;
            else caso = Caso.LR;
        } else {
            if (z == p.right) caso = Caso.RR;
            else caso = Caso.RL;
        }
        
        System.out.println("=> Caso " + caso + ": nodo está en posición " + 
                          (z == p.left ? "izquierda" : "derecha") + " del padre, y padre está en posición " +
                          (p == g.left ? "izquierda" : "derecha") + " del abuelo");
        return caso;
    }

     /**
      * Recolorea cuando el padre y el tío son rojos: pone padre y tío a negro,
      * abuelo a rojo y devuelve el nuevo nodo a procesar (el abuelo).
      *
      * @param z el nodo actual que se está reparando
      * @return el nodo al que debe desplazarse el algoritmo tras recolorear (normalmente el abuelo)
      */
     private RBNode<K,V> recolorearTioRojo(RBNode<K,V> z) {
        RBNode<K,V> p = z.parent;
        RBNode<K,V> g = p.parent;
        RBNode<K,V> tio = (p == g.left) ? g.right : g.left;
        if (p.color == RBNode.Color.ROJO && tio != NIL && tio.color == RBNode.Color.ROJO) {
            System.out.println("  > Recoloreando: padre y tío a negro, abuelo a rojo");
            p.color = RBNode.Color.NEGRO;
            tio.color = RBNode.Color.NEGRO;
            g.color = RBNode.Color.ROJO;
            System.out.println("\nDespués de recolorear:");
            mostrarArbol();
            return g; // subir z = g
        }
        return z; // no cambio
    }

     /**
      * Repara las violaciones de las propiedades del árbol rojo-negro después de
      * una inserción. Implementa el algoritmo clásico de recoloreo y rotaciones
      * hasta que se restauren las invariantes.
      *
      * @param z el nodo recientemente insertado (inicialmente rojo)
      */
     public void arreglarInsercion(RBNode<K,V> z) {
        // Algoritmo clásico: reparar violaciones hacia la raíz.
        while (z.parent != NIL && z.parent.color == RBNode.Color.ROJO) {
            RBNode<K,V> p = z.parent;
            RBNode<K,V> g = p.parent;
            if (g == NIL) break;
            RBNode<K,V> tio = (p == g.left) ? g.right : g.left;

            if (tio != NIL && tio.color == RBNode.Color.ROJO) {
                // Ejercicio 6: tío rojo -> recolorear (usar helper)
                System.out.println("\n=== Caso TÍO ROJO detectado ===");
                System.out.println("- Padre (" + p.key + ") y tío (" + tio.key + ") son rojos");
                System.out.println("- Cambiaremos: padre y tío a negro, abuelo a rojo");
                z = recolorearTioRojo(z);
            } else {
                // Distinguimos casos LL/LR y RR/RL
                if (p == g.left) {
                    if (z == p.right) { // LR
                        // Ejercicio 7: LR -> rotateLeft(p)
                        System.out.println("\n=== Caso LR detectado ===");
                        System.out.println("- Nodo (" + z.key + ") está a la derecha de su padre (" + p.key + ")");
                        System.out.println("- Realizando rotación izquierda en " + p.key);
                        rotIzquierda(p);
                        z = p; // tras rotateLeft, z apunta al antiguo p
                        p = z.parent;
                    }
                    // LL -> rotateRight(g) y recolorear
                    // Ejercicio 7: LL (o convertido a LL)
                    System.out.println("\nDespués de rotación izquierda:");
                    mostrarArbol();
                    
                    System.out.println("\n=== Caso LL detectado ===");
                    System.out.println("- Realizando rotación derecha en " + g.key);
                    System.out.println("- " + p.key + " subirá y será negro");
                    System.out.println("- " + g.key + " bajará y será rojo");
                    rotDerecha(g);
                    p = g.parent; // ahora p es la nueva sub-raíz
                    p.color = RBNode.Color.NEGRO;
                    g.color = RBNode.Color.ROJO;
                    z = p;
                    System.out.println("\nDespués de rotación derecha y recoloreo:");
                    mostrarArbol();
                } else {
                    if (z == p.left) { // RL
                        // Ejercicio 7: RL -> rotateRight(p)
                        System.out.println("\n=== Caso RL detectado ===");
                        System.out.println("- Nodo (" + z.key + ") está a la izquierda de su padre (" + p.key + ")");
                        System.out.println("- Realizando rotación derecha en " + p.key);
                        rotDerecha(p);
                        z = p;
                        p = z.parent;
                        System.out.println("\nDespués de rotación derecha:");
                        mostrarArbol();
                    }
                    // RR -> rotateLeft(g) y recolorear
                    System.out.println("\n=== Caso RR detectado ===");
                    System.out.println("- Realizando rotación izquierda en " + g.key);
                    System.out.println("- " + p.key + " subirá y será negro");
                    System.out.println("- " + g.key + " bajará y será rojo");
                    rotIzquierda(g);
                    p = g.parent;
                    p.color = RBNode.Color.NEGRO;
                    g.color = RBNode.Color.ROJO;
                    z = p;
                    System.out.println("\nDespués de rotación izquierda y recoloreo:");
                    mostrarArbol();
                }
            }
        }
        
        if (root.color != RBNode.Color.NEGRO) {
            System.out.println("\n=== Asegurando raíz negra ===");
            root.color = RBNode.Color.NEGRO;
            mostrarArbol();
        }
    }

     /**
      * Devuelve el nodo con la clave mínima en el subárbol cuyo root es `node`.
      *
      * @param node la raíz del subárbol a inspeccionar
      * @return el nodo mínimo (más a la izquierda)
      */
     public RBNode<K,V> minimo(RBNode<K,V> node) {
        while (node.left != NIL) node = node.left;
        return node;
    }

    /**
     * Devuelve el nodo con la clave máxima en el subárbol cuyo root es `node`.
     *
     * @param node la raíz del subárbol a inspeccionar
     * @return el nodo máximo (más a la derecha)
     */
    public RBNode<K,V> maximo(RBNode<K,V> node) {
        while (node.right != NIL) node = node.right;
        return node;
    }

    /**
     * Calcula el sucesor in-order de `x`: el menor nodo con clave estrictamente
     * mayor que `x.key`. Devuelve `NIL` si no existe.
     *
     * @param x el nodo objetivo
     * @return el sucesor in-order o `NIL` si no existe
     */
    public RBNode<K,V> sucesor(RBNode<K,V> x) {
        if (x == NIL) return NIL;
        if (x.right != NIL) return minimo(x.right);
        RBNode<K,V> y = x.parent;
        while (y != NIL && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * Calcula el predecesor in-order de `x`: el mayor nodo con clave estrictamente
     * menor que `x.key`. Devuelve `NIL` si no existe.
     *
     * @param x el nodo objetivo
     * @return el predecesor in-order o `NIL` si no existe
     */
    public RBNode<K,V> predecesor(RBNode<K,V> x) {
        if (x == NIL) return NIL;
        if (x.left != NIL) return maximo(x.left);
        RBNode<K,V> y = x.parent;
        while (y != NIL && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

     /**
      * Devuelve una lista enlazada con todas las claves en el rango cerrado [a,b]
      * en orden ascendente. Realiza un recorrido in-order acotado para evitar
      * visitar ramas irrelevantes.
      *
      * @param a límite inferior (inclusive)
      * @param b límite superior (inclusive)
      * @return una {@link ListaEnlazada} con las claves en el intervalo [a,b]
      */
     public ListaEnlazada<K> consultaRango(K a, K b) {
        ListaEnlazada<K> resultado = new ListaEnlazada<>();
        consultaRangoRec(root, a, b, resultado);
        return resultado;
    }

    private void consultaRangoRec(RBNode<K,V> node, K a, K b, ListaEnlazada<K> out) {
        if (node == NIL) return;
        if (node.key.compareTo(a) < 0) {
            // nodo.key < a => ir a derecha
            consultaRangoRec(node.right, a, b, out);
        } else if (node.key.compareTo(b) > 0) {
            // nodo.key > b => ir a izquierda
            consultaRangoRec(node.left, a, b, out);
        } else {
            // en rango
            consultaRangoRec(node.left, a, b, out);
            out.insertarFinal(node.key);
            consultaRangoRec(node.right, a, b, out);
        }
    }

    /**
     * Verifica que la raíz sea negra (propiedad obligatoria en RB-trees).
     *
     * @return true si la raíz es NIL o su color es NEGRO
     */
    public boolean raizNegra() {
        return root == NIL || root.color == RBNode.Color.NEGRO;
    }

    /**
     * Comprueba la propiedad "no rojo-rojo": si un nodo es rojo, sus hijos
     * deben ser negros (o NIL).
     *
     * @return true si la propiedad se mantiene en todo el árbol
     */
    public boolean sinRojoRojo() {
        return sinRojoRojoRec(root);
    }

    private boolean sinRojoRojoRec(RBNode<K,V> node) {
        if (node == NIL) return true;
        if (node.color == RBNode.Color.ROJO) {
            if (node.left.color != RBNode.Color.NEGRO || node.right.color != RBNode.Color.NEGRO) return false;
        }
        return sinRojoRojoRec(node.left) && sinRojoRojoRec(node.right);
    }

    /**
     * Calcula la altura negra (black-height) del árbol: el número de nodos
     * negros desde la raíz hasta las hojas. Si los caminos no tienen la misma
     * cantidad de nodos negros devuelve -1 indicando inconsistencia.
     *
     * @return black-height si consistente, -1 si no lo es
     */
    public int alturaNegra() {
        return alturaNegraRec(root);
    }

    private int alturaNegraRec(RBNode<K,V> node) {
        if (node == NIL) return 0;
        int left = alturaNegraRec(node.left);
        if (left == -1) return -1;
        int right = alturaNegraRec(node.right);
        if (right == -1) return -1;
        if (left != right) return -1;
        return left + (node.color == RBNode.Color.NEGRO ? 1 : 0);
    }

     /**
      * Busca un nodo por su clave en el árbol BST.
      *
      * @param key la clave a buscar
      * @return el nodo con la clave si existe, o `NIL` en caso contrario
      */
     public RBNode<K,V> buscar(K key) {
        RBNode<K,V> cur = root;
        while (cur != NIL) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            cur = cmp < 0 ? cur.left : cur.right;
        }
        return NIL;
    }

    /**
     * Devuelve una lista con las claves del árbol en recorrido in-order.
     * Útil para pruebas y para verificar el ordenamiento.
     *
     * @return {@link ListaEnlazada} con las claves en orden ascendente
     */
    public ListaEnlazada<K> obtenerEnOrdenClaves() {
        ListaEnlazada<K> out = new ListaEnlazada<>();
        inorderRec(root, out);
        return out;
    }

    private void inorderRec(RBNode<K,V> node, ListaEnlazada<K> out) {
        if (node == NIL) return;
        inorderRec(node.left, out);
        out.insertarFinal(node.key);
        inorderRec(node.right, out);
    }

    /**
     * Inserta una clave y valor en el árbol: primero realiza la inserción en
     * modo BST (rojo por defecto) y luego ejecuta el procedimiento de
     * reparación (recolor y rotaciones) para restaurar las propiedades RB.
     *
     * Este método es una envoltura útil en tests y demos.
     *
     * @param key la clave a insertar
     * @param val el valor asociado
     * @return el nodo insertado
     */
    public RBNode<K,V> insertar(K key, V val) {
        System.out.println("\n=== Insertando nodo " + key + " ===");
        RBNode<K,V> z = insertarABB(key, val);
        
        System.out.println("\nIniciando proceso de balanceo...");
        Caso caso = clasificarCaso(z);
        if (caso != Caso.NONE) {
            System.out.println("Caso detectado: " + caso);
            System.out.println("\nArbol antes de balancear):");
            mostrarArbol();

        }else {
            System.out.println("No se detectaron violaciones de propiedades.");
        }
        
        arreglarInsercion(z);
        return z;
    }


/* -------------------------------------------------
   Visualización horizontal del árbol (valores mayores arriba)
   ------------------------------------------------- */
    public void mostrarArbol() {
        if (root == NIL) {
            System.out.println("(vacío)");
        } else {
            mostrar(root, 0);
        }
    }

    private void mostrar(RBNode<K,V> nodo, int nivel) {
        if (nodo == NIL) return;

        // primero imprimir rama derecha (valores mayores)
        mostrar(nodo.right, nivel + 1);

        // margen según nivel
        for (int i = 0; i < nivel; i++) System.out.print("        ");
        System.out.println("(" + nodo.key + ":" + (nodo.color == RBNode.Color.ROJO ? "R" : "N") + ")");

        // luego rama izquierda (valores menores)
        mostrar(nodo.left, nivel + 1);
    }
}
