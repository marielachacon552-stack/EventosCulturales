package data;

public class Asiento {
    private int id;
    private int eventoId;
    private String numeroAsiento;
    private String estado;

    public Asiento() {}

    public Asiento(int eventoId, String numeroAsiento, String estado) {
        this.eventoId = eventoId;
        this.numeroAsiento = numeroAsiento;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    public String getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(String numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return numeroAsiento + " - " + estado;
    }
}
