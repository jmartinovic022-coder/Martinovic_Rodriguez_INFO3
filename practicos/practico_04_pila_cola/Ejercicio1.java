package practicos.practico_04_pila_cola;

import estructuras.pila.PilaArreglo;

public class Ejercicio1 {
    public static void main(String[] args) {
        PilaArreglo<Integer> pila = new PilaArreglo<>(5);

    System.out.println("¿Está vacía? " + pila.estaVacia());
        
    pila.apilar(10);
    pila.apilar(20);
    pila.apilar(30);
    pila.apilar(40);
    pila.apilar(50);

        pila.mostrar();
    System.out.println("¿Está llena? " + pila.estaLlena());
    pila.apilar(60);

        pila.mostrar();

    System.out.println("Elemento en cima(): " + pila.cima());

    System.out.println("Se sacó: " + pila.desapilar());
        pila.mostrar();
    System.out.println("Se sacó: " + pila.desapilar());
        pila.mostrar();

    System.out.println("¿Está vacía? " + pila.estaVacia());
    System.out.println("¿Está llena? " + pila.estaLlena());
    }
}
