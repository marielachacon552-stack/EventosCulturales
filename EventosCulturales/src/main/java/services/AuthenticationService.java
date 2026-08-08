package services;

import data.Usuario;
import database.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class AuthenticationService {
    private UsuarioDAO usuarioDAO;

    public AuthenticationService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(String correo, String contrasena) throws SQLException {
        Usuario usuario = usuarioDAO.findByCorreo(correo);
        if (usuario != null && BCrypt.checkpw(contrasena, usuario.getContrasena())) {
            return usuario;
        }
        return null;
    }

    public boolean registrar(Usuario usuario, String contrasenaPlana) throws SQLException {
        try {
            // Verificar que el correo no exista
            if (usuarioDAO.findByCorreo(usuario.getCorreo()) != null) {
                return false;
            }

            // Hashear la contraseña
            String contrasenaHasheada = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
            usuario.setContrasena(contrasenaHasheada);

            // Crear el usuario
            usuarioDAO.create(usuario);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarContrasena(int usuarioId, String contrasenaActual, String contrasenanueva) throws SQLException {
        Usuario usuario = usuarioDAO.read(usuarioId);
        if (usuario != null && BCrypt.checkpw(contrasenaActual, usuario.getContrasena())) {
            String nuevoHash = BCrypt.hashpw(contrasenanueva, BCrypt.gensalt());
            usuario.setContrasena(nuevoHash);
            usuarioDAO.update(usuario);
            return true;
        }
        return false;
    }
}
