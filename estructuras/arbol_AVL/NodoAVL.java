package estructuras.arbol_AVL;

/**
 * Nodo de un árbol binario de búsqueda AVL.
 * 
 * Representa un nodo dentro de la estructura de árbol AVL con información de balance.
 * Contiene referencias a subárboles izquierdo y derecho, altura del nodo y factor de equilibrio.
 * 
 * @author Info3 - Estructuras de Datos
 * @version 1.0
 */
public class NodoAVL {
    /** El valor entero almacenado en este nodo */
    int valor;
    
    /** Altura del nodo: distancia a la hoja más lejana (hoja = 0) */
    int altura;
    
    /** Factor de Equilibrio: diferencia entre altura del subárbol derecho e izquierdo (der.altura - izq.altura) */
    int fe;
    
    /** Referencia al hijo izquierdo (valores menores) */
    NodoAVL izq;
    
    /** Referencia al hijo derecho (valores mayores) */
    NodoAVL der;

    /**
     * Crea un nuevo nodo hoja con el valor especificado.
     * Inicializa altura en 0 (nodo hoja) y factor de equilibrio en 0.
     * 
     * @param valor el valor entero a almacenar en este nodo
     */
    public NodoAVL(int valor) {
        this.valor = valor;
        this.altura = 0; // Inicialmente, una hoja tiene altura 0
        this.fe = 0;
        this.izq = null;
        this.der = null;
    }

    /**
     * Establece el valor de este nodo.
     * 
     * @param valor el nuevo valor entero a almacenar
     */
    public void setValor(int valor) {
        this.valor = valor;
    }

    /**
     * Establece la altura de este nodo.
     * 
     * @param altura la nueva altura (distancia a la hoja más lejana)
     */
    public void setAltura(int altura) {
        this.altura = altura;
    }

    /**
     * Establece el factor de equilibrio de este nodo.
     * 
     * @param fe el nuevo factor de equilibrio (der.altura - izq.altura)
     */
    public void setFe(int fe) {
        this.fe = fe;
    }

    /**
     * Establece el hijo izquierdo de este nodo.
     * 
     * @param izq el nodo a asignar como hijo izquierdo (puede ser null)
     */
    public void setIzq(NodoAVL izq) {
        this.izq = izq;
    }

    /**
     * Establece el hijo derecho de este nodo.
     * 
     * @param der el nodo a asignar como hijo derecho (puede ser null)
     */
    public void setDer(NodoAVL der) {
        this.der = der;
    }

    /**
     * Obtiene el valor almacenado en este nodo.
     * 
     * @return el valor entero
     */
    public int getValor() {
        return valor;
    }

    /**
     * Obtiene la altura de este nodo.
     * 
     * @return la altura del nodo (hoja = 0)
     */
    public int getAltura() {
        return altura;
    }

    /**
     * Obtiene el factor de equilibrio de este nodo.
     * 
     * @return el factor de equilibrio (der.altura - izq.altura)
     */
    public int getFe() {
        return fe;
    }

    /**
     * Obtiene el hijo izquierdo de este nodo.
     * 
     * @return el nodo hijo izquierdo, o null si no tiene
     */
    public NodoAVL getIzq() {
        return izq;
    }

    /**
     * Obtiene el hijo derecho de este nodo.
     * 
     * @return el nodo hijo derecho, o null si no tiene
     */
    public NodoAVL getDer() {
        return der;
    }


}