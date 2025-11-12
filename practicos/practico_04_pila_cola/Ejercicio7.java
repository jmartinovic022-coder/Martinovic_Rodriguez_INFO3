package practicos.practico_04_pila_cola;

import estructuras.cola.ColaArreglo;


public class Ejercicio7 {
    public static void main(String[] args) {
        ColaArreglo<String> colaImpresion = new ColaArreglo<>(10);

    // Llegan 5 documentos
    colaImpresion.encolar("doc1.txt");
    colaImpresion.encolar("doc2.txt");
    colaImpresion.encolar("doc3.txt");
    colaImpresion.encolar("doc4.txt");
    colaImpresion.encolar("doc5.txt");

        System.out.println("Cola de impresión inicial:");
        colaImpresion.mostrar();

        // Se imprimen 3 documentos
        System.out.println("\n--- Procesando impresión ---");
        for (int i = 0; i < 3; i++) {
            String doc = colaImpresion.desencolar();
            if (doc != null) {
                System.out.println("Imprimiendo " + doc+"...");
            }
        }

        // Estado final de la cola
        System.out.println("\nCola de impresión después de imprimir 3 documentos:");
        colaImpresion.mostrar();
    }
}
