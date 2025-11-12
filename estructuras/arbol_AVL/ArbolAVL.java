package estructuras.arbol_AVL;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Árbol de Búsqueda Binaria Balanceado (AVL) basado en enteros.
 * 
 * Mantiene el balance automático mediante rotaciones simples (izquierda/derecha)
 * y rotaciones dobles (LR/RL) para garantizar que la altura del árbol sea O(log n).
 * 
 * Características:
 * - Soporta inserción, eliminación y búsqueda en O(log n)
 * - Detecta nodos críticos donde ocurrieron desbalances
 * - Cuenta rotaciones para análisis de rendimiento
 * - Valida la propiedad AVL del árbol
 * 
 * @author Proyecto Info3
 * @version 1.0
 */
public class ArbolAVL {
    private NodoAVL raiz;

    // Conjunto de nodos críticos donde surgió FE = ±2 durante inserciones
    private final Set<Integer> nodosCriticos = new HashSet<>();

    // Contadores de rotaciones para pruebas (encapsulados)
    private int rotacionesDerecha = 0;
    private int rotacionesIzquierda = 0;
    private int rotacionesLR = 0;
    private int rotacionesRL = 0;

    private int altura(NodoAVL n) {
        return (n == null) ? -1 : n.altura;
    }

    private void actualizarAltura(NodoAVL n) {
        if (n != null) {
            n.altura = 1 + Math.max(altura(n.izq), altura(n.der));
            n.fe = altura(n.izq) - altura(n.der);
        }
    }

    private NodoAVL rotacionDerecha(NodoAVL x) {
        rotacionesDerecha++;
        NodoAVL y = x.izq;
        x.izq = y.der;
        y.der = x;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    private NodoAVL rotacionIzquierda(NodoAVL y) {
        rotacionesIzquierda++;
        NodoAVL x = y.der;
        y.der = x.izq;
        x.izq = y;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    // Inserción (igual que antes)
    /**
     * Inserta un valor en el árbol AVL manteniendo el balance automático.
     * Si el valor ya existe, no se realiza la inserción (no se permiten duplicados).
     * 
     * @param valor el valor entero a insertar
     */
    public void insertar(int valor) {
        raiz = insertarRecursivo(raiz, valor);
    }

    private NodoAVL insertarRecursivo(NodoAVL nodo, int valor) {
        if (nodo == null)
            return new NodoAVL(valor);

        if (valor < nodo.valor)
            nodo.izq = insertarRecursivo(nodo.izq, valor);
        else if (valor > nodo.valor)
            nodo.der = insertarRecursivo(nodo.der, valor);
        else
            return nodo;

        actualizarAltura(nodo);

        // Casos LL, RR, LR, RL
        if (nodo.fe > 1 && valor < nodo.izq.valor) {
            nodosCriticos.add(nodo.valor);
            System.out.println("-> Desbalance en " + nodo.valor + " (LL). Rotación Derecha.");
            return rotacionDerecha(nodo);
        }
        if (nodo.fe < -1 && valor > nodo.der.valor) {
            nodosCriticos.add(nodo.valor);
            System.out.println("-> Desbalance en " + nodo.valor + " (RR). Rotación Izquierda.");
            return rotacionIzquierda(nodo);
        }
        if (nodo.fe > 1 && valor > nodo.izq.valor) {
            nodosCriticos.add(nodo.valor);
            rotacionesLR++;
            System.out.println("-> Desbalance en " + nodo.valor + " (LR). Rotación Izquierda + Derecha.");
            nodo.izq = rotacionIzquierda(nodo.izq);
            return rotacionDerecha(nodo);
        }
        if (nodo.fe < -1 && valor < nodo.der.valor) {
            nodosCriticos.add(nodo.valor);
            rotacionesRL++;
            System.out.println("-> Desbalance en " + nodo.valor + " (RL). Rotación Derecha + Izquierda.");
            nodo.der = rotacionDerecha(nodo.der);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // -------------------
    // Eliminación pública
    // -------------------
    // Eliminación pública
    // -------------------
    /**
     * Elimina un valor del árbol AVL manteniendo el balance automático.
     * Si el valor no existe, el árbol permanece sin cambios.
     * 
     * @param valor el valor entero a eliminar
     */
    public void eliminar(int valor) {
        raiz = eliminarRec(raiz, valor);
    }

    private NodoAVL eliminarRec(NodoAVL nodo, int valor) {
        if (nodo == null) return null;

        if (valor < nodo.valor) {
            nodo.izq = eliminarRec(nodo.izq, valor);
        } else if (valor > nodo.valor) {
            nodo.der = eliminarRec(nodo.der, valor);
        } else {
            // encontrado
            if (nodo.izq == null && nodo.der == null) {
                return null; // hoja
            } else if (nodo.izq == null) {
                return nodo.der;
            } else if (nodo.der == null) {
                return nodo.izq;
            } else {
                // dos hijos: reemplazar con sucesor inorder (mínimo en subárbol derecho)
                NodoAVL sucesor = minNodo(nodo.der);
                nodo.valor = sucesor.valor;
                nodo.der = eliminarRec(nodo.der, sucesor.valor);
            }
        }

        // actualizar y balancear igual que en inserción
        actualizarAltura(nodo);

        if (nodo.fe > 1 && getFE(nodo.izq) >= 0) {
            System.out.println("-> Desbalance en " + nodo.valor + " (LL tras borrado). Rotación Derecha.");
            return rotacionDerecha(nodo);
        }
        if (nodo.fe > 1 && getFE(nodo.izq) < 0) {
            System.out.println("-> Desbalance en " + nodo.valor + " (LR tras borrado). Rotación Izquierda + Derecha.");
            nodo.izq = rotacionIzquierda(nodo.izq);
            return rotacionDerecha(nodo);
        }
        if (nodo.fe < -1 && getFE(nodo.der) <= 0) {
            System.out.println("-> Desbalance en " + nodo.valor + " (RR tras borrado). Rotación Izquierda.");
            return rotacionIzquierda(nodo);
        }
        if (nodo.fe < -1 && getFE(nodo.der) > 0) {
            System.out.println("-> Desbalance en " + nodo.valor + " (RL tras borrado). Rotación Derecha + Izquierda.");
            nodo.der = rotacionDerecha(nodo.der);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    private NodoAVL minNodo(NodoAVL n) {
        NodoAVL curr = n;
        while (curr.izq != null) curr = curr.izq;
        return curr;
    }

    private int getFE(NodoAVL n) {
        return (n == null) ? 0 : n.fe;
    }

    // -------------------
    // Recorridos y dibujo
    // -------------------
    /**
     * Recorre el árbol en orden (inorden) mostrando valores, alturas y factor de equilibrio.
     * Marca con [CRIT] los nodos donde ocurrieron desbalances.
     */
    public void inorden() {
        inordenRecursivo(raiz);
        System.out.println();
    }

    private void inordenRecursivo(NodoAVL nodo) {
        if (nodo != null) {
            inordenRecursivo(nodo.izq);
            String marcador = nodosCriticos.contains(nodo.valor) ? " [CRIT]" : "";
            System.out.print(nodo.valor + "(FE:" + nodo.fe + ", h:" + nodo.altura + ")" + marcador + " ");
            inordenRecursivo(nodo.der);
        }
    }

    /**
     * Limpia el conjunto de nodos críticos antes de una nueva secuencia de operaciones.
     * Útil para análisis repetidos.
     */
    public void limpiarNodosCriticos() {
        nodosCriticos.clear();
    }

    /**
     * Obtiene una copia inmodificable del conjunto de nodos críticos detectados.
     * Nodos críticos son aquellos donde ocurrió un desbalance (FE = ±2).
     * 
     * @return conjunto de valores de nodos donde ocurrieron desbalances
     */
    public Set<Integer> getNodosCriticos() {
        return Collections.unmodifiableSet(new HashSet<>(nodosCriticos));
    }

    /**
     * Dibuja el árbol horizontalmente con una representación visual.
     * Muestra altura y factor de equilibrio de cada nodo.
     */
    public void dibujar() {
        System.out.println("\nRepresentación del Árbol (horizontal):");
        dibujarHorizontal(raiz, 0);
        System.out.println();
    }

    private void dibujarHorizontal(NodoAVL nodo, int nivel) {
        if (nodo != null) {
            dibujarHorizontal(nodo.der, nivel + 1);
            for (int i = 0; i < nivel; i++)
                System.out.print("    ");
            System.out.println("↓ " + nodo.valor + " (h:" + nodo.altura + ", FE:" + nodo.fe + ")");
            dibujarHorizontal(nodo.izq, nivel + 1);
        }
    }

    /**
     * Resultado de validación AVL: contiene el estado de validez y la altura del subárbol.
     * Usada por esAVL para retornar múltiples valores durante la validación recursiva.
     */
    public static class ResultadoAVL {
        public final boolean es;
        public final int altura;
        public ResultadoAVL(boolean es, int altura) {
            this.es = es; this.altura = altura;
        }
    }

    /**
     * Valida si un árbol con raíz en el nodo dado cumple las propiedades AVL.
     * Verifica: balance entre subárboles (|FE| ≤ 1), propiedad ABB, y alturas correctas.
     * 
     * @param r el nodo raíz del subárbol a validar
     * @return ResultadoAVL con es=true si es válido, y altura correcta del subárbol
     */
    public ResultadoAVL esAVL(NodoAVL r) {
        return esAVLRec(r);
    }

    /**
     * Valida si el árbol completo (desde la raíz actual) cumple todas las propiedades AVL.
     * Wrapper que verifica todo el árbol.
     * 
     * @return ResultadoAVL con es=true si todo el árbol es válido
     */
    public ResultadoAVL esAVL() {
        return esAVL(raiz);
    }

    private ResultadoAVL esAVLRec(NodoAVL nodo) {
        if (nodo == null) return new ResultadoAVL(true, -1);

        ResultadoAVL izq = esAVLRec(nodo.izq);
        ResultadoAVL der = esAVLRec(nodo.der);

        // Si alguna subrama no es AVL, propagar falso
        if (!izq.es || !der.es) return new ResultadoAVL(false, 0);

        // verificar FE
        if (Math.abs(izq.altura - der.altura) > 1) return new ResultadoAVL(false, 0);

        // verificar propiedad de ABB
        if (nodo.izq != null && nodo.izq.valor > nodo.valor) return new ResultadoAVL(false, 0);
        if (nodo.der != null && nodo.der.valor < nodo.valor) return new ResultadoAVL(false, 0);

        int h = 1 + Math.max(izq.altura, der.altura);
        return new ResultadoAVL(true, h);
    }

    /**
     * Obtiene el nodo raíz del árbol.
     * Útil para pruebas y validaciones externas del árbol completo.
     * 
     * @return el nodo raíz, o null si el árbol está vacío
     */
    public NodoAVL getRaiz() {
        return raiz;
    }

    /**
     * Reinicia todos los contadores de rotaciones a cero.
     * Debe llamarse antes de medir rotaciones en una nueva secuencia de operaciones.
     */
    public void resetRotaciones() {
        rotacionesDerecha = 0;
        rotacionesIzquierda = 0;
        rotacionesLR = 0;
        rotacionesRL = 0;
    }

    /**
     * Obtiene el contador de rotaciones derechas simples realizadas.
     * @return número de rotaciones derechas
     */
    public int getRotacionesDerecha() { 
        return rotacionesDerecha; 
    }

    /**
     * Obtiene el contador de rotaciones izquierdas simples realizadas.
     * @return número de rotaciones izquierdas
     */
    public int getRotacionesIzquierda() { 
        return rotacionesIzquierda; 
    }

    /**
     * Obtiene el contador de rotaciones izquierda-derecha (LR) realizadas.
     * @return número de rotaciones LR
     */
    public int getRotacionesLR() { 
        return rotacionesLR; 
    }

    /**
     * Obtiene el contador de rotaciones derecha-izquierda (RL) realizadas.
     * @return número de rotaciones RL
     */
    public int getRotacionesRL() { 
        return rotacionesRL; 
    }
    /**
     * Calcula el total de rotaciones simples ejecutadas (izquierda + derecha).
     * Los contadores rotacionesLR/rotacionesRL representan eventos compuestos
     * (una rotación doble) y NO se suman aquí para evitar doble conteo.
     * 
     * @return suma de rotaciones derechas e izquierdas
     */
    public int getTotalRotaciones() { 
        return rotacionesDerecha + rotacionesIzquierda; 
    }
}
