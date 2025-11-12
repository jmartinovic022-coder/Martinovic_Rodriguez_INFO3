package practicos.practico_03_ordenamiento.src;

import estructuras.lista_enlazada.ListaEnlazada;

public class Pizzeria {
    private final ListaEnlazada<Pedido> pedidos = new ListaEnlazada<>();

    public void agregarPedido(Pedido pedido) {
        pedidos.insertarFinal(pedido);
    }

    public void eliminarPedido(String nombreCliente) {
        // eliminar de atrás hacia adelante para mantener índices válidos
        for (int i = pedidos.contar() - 1; i >= 0; i--) {
            Pedido p = pedidos.obtener(i);
            if (p.getNombreCliente().equalsIgnoreCase(nombreCliente)) {
                pedidos.eliminarEn(i);
            }
        }
    }

    public void actualizarPedido(String nombreCliente, int nuevoTiempo, double nuevoPrecio) {
        for (int i = 0; i < pedidos.contar(); i++) {
            Pedido p = pedidos.obtener(i);
            if (p.getNombreCliente().equalsIgnoreCase(nombreCliente)) {
                p.setTiempoPreparacion(nuevoTiempo);
                p.setPrecioTotal(nuevoPrecio);
                break;
            }
        }
    }

    public ListaEnlazada<Pedido> getPedidos() {
        return pedidos;
    }

    public void mostrarPedidos() {
        for (int i = 0; i < pedidos.contar(); i++) {
            System.out.println(pedidos.obtener(i));
        }
    }
}
