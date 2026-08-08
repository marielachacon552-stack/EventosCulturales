package beans;

import data.Usuario;
import database.UsuarioDAO;
import database.RoleDAO;
import services.AuthenticationService;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import java.io.Serializable;
import java.sql.SQLException;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private String correo;
    private String contrasena;
    private Usuario usuarioActual;
    private AuthenticationService authService;
    private RoleDAO roleDAO;

    public LoginBean() {
        this.authService = new AuthenticationService();
        this.roleDAO = new RoleDAO();
    }

    public String login() {
        try {
            usuarioActual = authService.login(correo, contrasena);
            
            if (usuarioActual != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioActual", usuarioActual);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioId", usuarioActual.getId());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("roleId", usuarioActual.getRoleId());
                
                correo = "";
                contrasena = "";
                return "redirect:dashboard.xhtml";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Correo o contraseña incorrectos"));
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error en la autenticación"));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "redirect:login.xhtml";
    }

    public boolean isLogueado() {
        return usuarioActual != null;
    }

    public Usuario getUsuarioActual() {
        if (usuarioActual == null) {
            usuarioActual = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioActual");
        }
        return usuarioActual;
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

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}
