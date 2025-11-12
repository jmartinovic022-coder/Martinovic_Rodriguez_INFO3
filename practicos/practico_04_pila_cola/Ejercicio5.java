package practicos.practico_04_pila_cola;

import java.util.*;

import estructuras.cola.ColaArreglo;
import estructuras.pila.PilaArreglo;

public class Ejercicio5 {
    public static void main(String[] args) throws Exception {
        String palabra;
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese una palabra para checkear si es palindromo: ");
        palabra = sc.nextLine();
        PilaArreglo<Character> pila = new PilaArreglo<>(palabra.length());
        ColaArreglo<Character> cola = new ColaArreglo<>(palabra.length());
        for(int i = 0; i < palabra.length(); i++) {
            pila.apilar(palabra.charAt(i));
            cola.encolar(palabra.charAt(i));
        }
        for(int i = 0; i < palabra.length(); i++) {
            if(pila.desapilar() != cola.desencolar()) {
                System.out.println("La palabra " + palabra + " no es un palíndromo.");
                sc.close();
                return;
            }
        }
        System.out.println("La palabra " + palabra + " es un palíndromo.");
        sc.close();
    }
}
