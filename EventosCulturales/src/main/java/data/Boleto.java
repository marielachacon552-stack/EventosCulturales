package data;

import java.time.LocalDateTime;

public class Boleto {
    private int id;
    private int eventoId;
    private int usuarioId;
    private String numeroAsiento;
    private String estado;
    private LocalDateTime fechaCompra;
    private String codigoBoleto;

    public Boleto() {}

    public Boleto(int eventoId, int usuarioId, String numeroAsiento) {
        this.eventoId = eventoId;
        this.usuarioId = usuarioId;
        this.numeroAsiento = numeroAsiento;
        this.estado = "activo";
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

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
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

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getCodigoBoleto() {
        return codigoBoleto;
    }

    public void setCodigoBoleto(String codigoBoleto) {
        this.codigoBoleto = codigoBoleto;
    }

    @Override
    public String toString() {
        return "Boleto [" + codigoBoleto + "] - Asiento " + numeroAsiento;
    }
}
