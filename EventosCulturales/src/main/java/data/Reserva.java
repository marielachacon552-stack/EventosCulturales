package data;

import java.time.LocalDateTime;

public class Reserva {
    private int id;
    private int eventoId;
    private int usuarioId;
    private LocalDateTime fechaReserva;
    private String estado;
    private int cantidadBoletos;
    private double totalPagado;

    public Reserva() {}

    public Reserva(int eventoId, int usuarioId, int cantidadBoletos, double totalPagado) {
        this.eventoId = eventoId;
        this.usuarioId = usuarioId;
        this.cantidadBoletos = cantidadBoletos;
        this.totalPagado = totalPagado;
        this.estado = "activa";
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

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantidadBoletos() {
        return cantidadBoletos;
    }

    public void setCantidadBoletos(int cantidadBoletos) {
        this.cantidadBoletos = cantidadBoletos;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    @Override
    public String toString() {
        return "Reserva #" + id + " - " + cantidadBoletos + " boletos";
    }
}
