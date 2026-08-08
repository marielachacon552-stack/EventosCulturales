package beans;

import data.Usuario;
import data.Role;
import database.UsuarioDAO;
import database.RoleDAO;
import services.AuthenticationService;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.mindrot.jbcrypt.BCrypt;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Named(value = "usuariosBean")
@SessionScoped
public class UsuariosBean implements Serializable {
    private UsuarioDAO usuarioDAO;
    private RoleDAO roleDAO;
    private AuthenticationService authService;
    
    private List<Usuario> usuarios;
    private List<Role> roles;
    private Usuario usuarioSeleccionado;
    private Usuario usuarioNuevo;
    private String contrasenaNueva;
    private boolean mostrarDialogo;

    public UsuariosBean() {
        this.usuarioDAO = new UsuarioDAO();
        this.roleDAO = new RoleDAO();
        this.authService = new AuthenticationService();
        this.usuarioNuevo = new Usuario();
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            usuarios = usuarioDAO.readAll();
            roles = roleDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void abrirDialogoNuevo() {
        usuarioNuevo = new Usuario();
        contrasenaNueva = "";
        mostrarDialogo = true;
    }

    public void abrirDialogoEditar() {
        if (usuarioSeleccionado != null) {
            usuarioNuevo = new Usuario();
            usuarioNuevo.setId(usuarioSeleccionado.getId());
            usuarioNuevo.setNombre(usuarioSeleccionado.getNombre());
            usuarioNuevo.setCorreo(usuarioSeleccionado.getCorreo());
            usuarioNuevo.setRoleId(usuarioSeleccionado.getRoleId());
            contrasenaNueva = "";
            mostrarDialogo = true;
        }
    }

    public void guardar() {
        if (usuarioNuevo.getNombre() == null || usuarioNuevo.getNombre().trim().isEmpty() ||
            usuarioNuevo.getCorreo() == null || usuarioNuevo.getCorreo().trim().isEmpty() ||
            usuarioNuevo.getRoleId() == 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Validación", "Por favor complete todos los campos"));
            return;
        }

        try {
            if (usuarioNuevo.getId() == 0) {
                // Crear nuevo
                if (contrasenaNueva == null || contrasenaNueva.trim().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Validación", "Ingrese una contraseña"));
                    return;
                }
                
                if (authService.registrar(usuarioNuevo, contrasenaNueva)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario creado correctamente"));
                    mostrarDialogo = false;
                    cargarDatos();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El correo ya existe"));
                }
            } else {
                // Actualizar
                usuarioDAO.update(usuarioNuevo);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario actualizado correctamente"));
                mostrarDialogo = false;
                cargarDatos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar usuario"));
        }
    }

    public void eliminar() {
        if (usuarioSeleccionado != null) {
            try {
                usuarioDAO.delete(usuarioSeleccionado.getId());
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario eliminado correctamente"));
                cargarDatos();
                usuarioSeleccionado = null;
            } catch (SQLException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar usuario"));
            }
        }
    }

    public void cancelar() {
        mostrarDialogo = false;
        usuarioNuevo = new Usuario();
    }

    // Getters y Setters
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public Usuario getUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(Usuario usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public String getContrasenaNueva() {
        return contrasenaNueva;
    }

    public void setContrasenaNueva(String contrasenaNueva) {
        this.contrasenaNueva = contrasenaNueva;
    }

    public boolean isMostrarDialogo() {
        return mostrarDialogo;
    }

    public void setMostrarDialogo(boolean mostrarDialogo) {
        this.mostrarDialogo = mostrarDialogo;
    }
}
