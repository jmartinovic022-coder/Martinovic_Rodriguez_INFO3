
package practicos.practico_09_integrador.src;
import estructuras.pila.PilaDinamica;
public class AgendaConHistorial extends AgendaMedicoImpl implements AgendaConHistorialInterface {
    private final PilaDinamica<Comando> pilaUndo;
    private final PilaDinamica<Comando> pilaRedo;

    public AgendaConHistorial() {
        super();
    this.pilaUndo = new PilaDinamica<>();
    this.pilaRedo = new PilaDinamica<>();
    }

    // Comando interface
    public interface Comando {
        boolean execute();
        boolean unexecute();
    }

    // Helper methods to call superclass implementations directly
    protected boolean doAgendar(Turno t) {
        return super.agendar(t);
    }

    protected boolean doCancelar(String id) {
        return super.cancelar(id);
    }

    // AgendarComando
    private class AgendarComando implements Comando {
        private final Turno t;

        AgendarComando(Turno t) { this.t = t; }

        @Override
        public boolean execute() { return doAgendar(t); }

        @Override
        public boolean unexecute() { return doCancelar(t.getId()); }
    }

    // CancelarComando
    private class CancelarComando implements Comando {
        private final Turno t;

        CancelarComando(Turno t) { this.t = t; }

        @Override
        public boolean execute() { return doCancelar(t.getId()); }

        @Override
        public boolean unexecute() { return doAgendar(t); }
    }

    @Override
    public boolean agendar(Turno t) {
        if (t == null) return false;
        Comando c = new AgendarComando(t);
        boolean ok = c.execute();
        if (ok) {
            pilaUndo.apilar(c);
            // clear redo
            while (!pilaRedo.estaVacia()) pilaRedo.desapilar();
        }
        return ok;
    }

    @Override
    public boolean cancelar(String idTurno) {
        if (idTurno == null) return false;
        // find turno by id
        Turno found = null;
        for (Turno t : this.avl.obtenerEnOrden()) {
            if (idTurno.equals(t.getId())) { found = t; break; }
        }
        if (found == null) return false;
        Comando c = new CancelarComando(found);
        boolean ok = c.execute();
        if (ok) {
            pilaUndo.apilar(c);
            while (!pilaRedo.estaVacia()) pilaRedo.desapilar();
        }
        return ok;
    }

    @Override
    public void undo() {
        if (pilaUndo.estaVacia()) return;
        Comando c = pilaUndo.desapilar();
        boolean ok = c.unexecute();
        if (ok) pilaRedo.apilar(c);
    }

    @Override
    public void redo() {
        if (pilaRedo.estaVacia()) return;
        Comando c = pilaRedo.desapilar();
        boolean ok = c.execute();
        if (ok) pilaUndo.apilar(c);
    }
}
