package practicos.practico_04_pila_cola;

import estructuras.cola.ColaArreglo;

public class Ejercicio4 {
    public static void main(String[] args) {
        ColaArreglo<String> cola = new ColaArreglo<>(10);

    cola.encolar("Ana");
    cola.encolar("Luis");
    cola.encolar("Marta");
    cola.encolar("Pedro");

        System.out.println("Cola inicial:");
        cola.mostrar();

    System.out.println("Atendido: " + cola.desencolar());
    System.out.println("Atendido: " + cola.desencolar());

        System.out.println("Cola después de atender:");
        cola.mostrar();
    }
}

