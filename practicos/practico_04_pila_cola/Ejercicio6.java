package practicos.practico_04_pila_cola;

import java.util.*;

import estructuras.pila.PilaArreglo;

public class Ejercicio6 {
    public static void main(String[] args) throws Exception {
        PilaArreglo<String> pilaUndo = new PilaArreglo<>(5);
        PilaArreglo<String> pilaRedo = new PilaArreglo<>(5);
        int opc;
        Scanner sc = new Scanner(System.in);
        do{
            System.out.println("\n1. Ejecutar acción");
            System.out.println("2. Deshacer acción");
            System.out.println("3. Rehacer acción");
            System.out.println("4. Mostrar estado de las pilas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opc = Integer.parseInt(sc.nextLine());
            switch(opc){
                case 1:
                    System.out.print("Ingrese la acción a ejecutar: ");
                    String accion = sc.nextLine();
                    pilaUndo.apilar(accion);
                    // Limpiar la pila de redo al ejecutar una nueva acción
                    pilaRedo = new PilaArreglo<>(5);
                    break;
                case 2:
                    if(pilaUndo.estaVacia()) {
                        System.out.println("\nNo hay acciones para deshacer.\n");
                        break;
                    }
                    System.out.println("\nSe deshizo la última acción.\n");
                    String deshacerAccion = pilaUndo.desapilar();
                    if(deshacerAccion != null) {
                        pilaRedo.apilar(deshacerAccion);
                    }
                    break;
                case 3:
                    if(pilaRedo.estaVacia()) {
                        System.out.println("\nNo hay acciones para rehacer.\n");
                        break;
                    }
                    System.out.println("\nSe rehizo la última acción.\n");
                    String rehacerAccion = pilaRedo.desapilar();
                    if(rehacerAccion != null) {
                        pilaUndo.apilar(rehacerAccion);
                    }
                    break;
                case 4:
                    System.out.println("\nEstado de la pila de Deshacer:");
                    pilaUndo.mostrar();
                    System.out.println("\nEstado de la pila de Rehacer:");
                    pilaRedo.mostrar();
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida, intente de nuevo.");
            }
        }while(opc!=5);
        sc.close();
    }
}
