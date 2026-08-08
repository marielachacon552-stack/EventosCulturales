package data;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;
    private int roleId;
    private LocalDateTime fechaRegistro;

    public Usuario() {}

    public Usuario(String nombre, String correo, String contrasena, int roleId) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.roleId = roleId;
    }

    public Usuario(int id, String nombre, String correo, String contrasena, int roleId) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return nombre + " (" + correo + ")";
    }
}
