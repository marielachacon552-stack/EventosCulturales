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
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.findByCorreo(correo);
        if (usuario != null) {
            System.out.println("Usuario encontrado: " + usuario.getCorreo());
            System.out.println("Contrasena/Hash en BD: " + usuario.getContrasena());

            boolean match = false;
            if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$")) {
                match = contrasena.equals(usuario.getContrasena());
            } else {
                match = BCrypt.checkpw(contrasena, usuario.getContrasena());
            }

            System.out.println("Coincide contraseña: " + match);
            if (match) {
                return usuario;
            }
        }
        return null;
    }
    public boolean registrar(Usuario usuario, String contrasenaPlana) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();


        if (usuarioDAO.findByCorreo(usuario.getCorreo()) != null) {
            return false;
        }

        String hashContrasena = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
        usuario.setContrasena(hashContrasena);

        return usuarioDAO.create(usuario);
    }
}
