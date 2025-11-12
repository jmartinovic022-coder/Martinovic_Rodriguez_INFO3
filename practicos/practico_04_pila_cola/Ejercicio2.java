package practicos.practico_04_pila_cola;

import estructuras.cola.ColaArreglo;

public class Ejercicio2 {
        public static void main(String[] args) {
        ColaArreglo<Integer> cola = new ColaArreglo<>(5);

        cola.encolar(1);
        cola.encolar(2);
        cola.encolar(3);
        cola.encolar(4);
        cola.mostrar();

        System.out.println("Elemento en verFrente(): " + cola.verFrente());

        System.out.println("Se desencoló: " + cola.desencolar());
        cola.mostrar();

        System.out.println("¿Está vacía? " + cola.estaVacia());
        System.out.println("¿Está llena? " + cola.estaLlena());
    }
}
