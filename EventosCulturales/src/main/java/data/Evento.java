package data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Evento implements Serializable {
    private int id;
    private String titulo;
    private String categoria;
    private LocalDateTime fechaHora;
    private int duracion;
    private String descripcion;
    private double precioBoleto;
    private int aforoMaximo;
    private String ubicacion;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Evento() {}

    public Evento(String titulo, String categoria, LocalDateTime fechaHora, int duracion,
                  String descripcion, double precioBoleto, int aforoMaximo, String ubicacion) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.precioBoleto = precioBoleto;
        this.aforoMaximo = aforoMaximo;
        this.ubicacion = ubicacion;
        this.estado = "activo";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioBoleto() {
        return precioBoleto;
    }

    public void setPrecioBoleto(double precioBoleto) {
        this.precioBoleto = precioBoleto;
    }

    public int getAforoMaximo() {
        return aforoMaximo;
    }

    public void setAforoMaximo(int aforoMaximo) {
        this.aforoMaximo = aforoMaximo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaHoraFormateada() {
        if (this.fechaHora != null) {
            return this.fechaHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'Hora:' HH:mm"));
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return id == evento.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return titulo + " (" + categoria + ")";
    }
}