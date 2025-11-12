package practicos.practico_02_recursividad;
import java.util.Scanner;
/* 
Escriba una función recursiva que calcule cuántos dígitos tiene un número entero positivo.
Ejemplo: 12345 → 5. 
*/

public class Ejercicio1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numero; 
        System.out.print("Ingrese un número entero positivo: ");
        numero = sc.nextInt();
        int resultado = conteo(numero);
        System.out.println("El número " + numero + " tiene " + resultado + " dígitos.");
        sc.close();
    }
    public static int conteo(int x){
        // Manejar negativos y el caso extremo Integer.MIN_VALUE sin overflow
        long y = Math.abs((long) x);
        return conteoLong(y);
    }

    // Helper que opera sobre long para evitar problemas con Integer.MIN_VALUE
    private static int conteoLong(long y) {
        if (y < 10) {
            return 1;
        } else {
            return 1 + conteoLong(y / 10);
        }
    }
}
