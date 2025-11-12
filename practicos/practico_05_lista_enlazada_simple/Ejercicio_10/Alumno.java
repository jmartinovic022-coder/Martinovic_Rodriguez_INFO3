package practicos.practico_05_lista_enlazada_simple.Ejercicio_10;

public class Alumno {
    private final String nombre;
    private final int legajo;

    public Alumno(String nombre, int legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getLegajo() {
        return legajo;
    }

    @Override
    public String toString() {
        return "[" + legajo + " - " + nombre + "]";
    }

    // Igualdad basada en el legajo
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Alumno)) return false;
        Alumno otro = (Alumno) obj;
        return this.legajo == otro.legajo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(legajo);
    }
}
