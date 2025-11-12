package practicos.practico_04_pila_cola;
import java.util.*;

import estructuras.pila.PilaArreglo;


public class Ejercicio3 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String palabra;
        System.out.println("Ingrese la palabra que desea ver al reves: ");
        palabra = sc.nextLine();
        PilaArreglo<Character> pila = new PilaArreglo<>(palabra.length());
        for(int i=0; i<palabra.length(); i++) {
            pila.apilar(palabra.charAt(i));
        }
        System.out.println("Palabra original: " + palabra);
        pila.escribirPalabraInvertida();
        sc.close();
    }
}
