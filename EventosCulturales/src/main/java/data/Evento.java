package data;

import java.time.LocalDateTime;

public class Evento {
    private int id;
    private String titulo;
    private String categoria;
    private LocalDateTime fechaHora;
    private int duracion;
    private String descripcion;
    private double precioBoieto;
    private int aforoMaximo;
    private String ubicacion;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Evento() {}

    public Evento(String titulo, String categoria, LocalDateTime fechaHora, int duracion,
                  String descripcion, double precioBoieto, int aforoMaximo, String ubicacion) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.precioBoieto = precioBoieto;
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

    public double getPrecioBoieto() {
        return precioBoieto;
    }

    public void setPrecioBoieto(double precioBoieto) {
        this.precioBoieto = precioBoieto;
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

    @Override
    public String toString() {
        return titulo + " (" + categoria + ")";
    }
}
