package practicos.practico_03_ordenamiento.src;
import estructuras.lista_enlazada.ListaEnlazada;

public class Ordenador {

    // Inserción → ordenar por tiempo de preparación
    public void insercionPorTiempo(ListaEnlazada<Pedido> pedidos) {
        for (int i = 1; i < pedidos.contar(); i++) {
            Pedido key = pedidos.obtener(i);
            int j = i - 1;
            while (j >= 0 && pedidos.obtener(j).getTiempoPreparacion() > key.getTiempoPreparacion()) {
                pedidos.setEn(j + 1, pedidos.obtener(j));
                j--;
            }
            pedidos.setEn(j + 1, key);
        }
    }

    // Shellsort → ordenar por precio total
    public void shellsortPorPrecio(ListaEnlazada<Pedido> pedidos) {
        int n = pedidos.contar();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Pedido temp = pedidos.obtener(i);
                int j;
                for (j = i; j >= gap && pedidos.obtener(j - gap).getPrecioTotal() > temp.getPrecioTotal(); j -= gap) {
                    pedidos.setEn(j, pedidos.obtener(j - gap));
                }
                pedidos.setEn(j, temp);
            }
        }
    }

    // Quicksort → ordenar por nombre del cliente
    public void quicksortPorNombre(ListaEnlazada<Pedido> pedidos, int low, int high) {
        if (low < high) {
            int pi = partition(pedidos, low, high);
            quicksortPorNombre(pedidos, low, pi - 1);
            quicksortPorNombre(pedidos, pi + 1, high);
        }
    }

    private int partition(ListaEnlazada<Pedido> pedidos, int low, int high) {
        String pivot = pedidos.obtener(high).getNombreCliente();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (pedidos.obtener(j).getNombreCliente().compareToIgnoreCase(pivot) <= 0) {
                i++;
                Pedido temp = pedidos.obtener(i);
                pedidos.setEn(i, pedidos.obtener(j));
                pedidos.setEn(j, temp);
            }
        }
        Pedido temp = pedidos.obtener(i + 1);
        pedidos.setEn(i + 1, pedidos.obtener(high));
        pedidos.setEn(high, temp);

        return i + 1;
    }
}
