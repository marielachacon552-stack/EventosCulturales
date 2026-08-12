package services;

import data.*;
import database.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CompraService {
    private EventoDAO eventoDAO;
    private AsientoDAO asientoDAO;
    private BoletoDAO boletoDAO;
    private ReservaDAO reservaDAO;
    private EmailService emailService;

    public CompraService() {
        this.eventoDAO = new EventoDAO();
        this.asientoDAO = new AsientoDAO();
        this.boletoDAO = new BoletoDAO();
        this.reservaDAO = new ReservaDAO();
        this.emailService = new EmailService();
    }

    public boolean validarDisponibilidad(int eventoId, List<String> asientos) throws SQLException {
        Evento evento = eventoDAO.read(eventoId);
        if (evento == null || !evento.getEstado().equals("activo")) {
            return false;
        }

        List<Asiento> asientosDisp = asientoDAO.findDisponiblesByEvento(eventoId);
        for (String numeroAsiento : asientos) {
            boolean encontrado = false;
            for (Asiento a : asientosDisp) {
                if (a.getNumeroAsiento().equals(numeroAsiento)) {
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                return false;
            }
        }
        return true;
    }

    public Reserva procesarCompra(int usuarioId, int eventoId, List<String> asientos, UsuarioDAO usuarioDAO) throws SQLException {
        try {
            // Validar disponibilidad
            if (!validarDisponibilidad(eventoId, asientos)) {
                return null;
            }

            Evento evento = eventoDAO.read(eventoId);
            double totalPagado = evento.getPrecioBoleto() * asientos.size();

            // Crear boletos
            for (String numeroAsiento : asientos) {
                Boleto boleto = new Boleto(eventoId, usuarioId, numeroAsiento);
                boleto.setCodigoBoleto(UUID.randomUUID().toString().substring(0, 12).toUpperCase());
                boleto.setFechaCompra(LocalDateTime.now());
                boleto.setEstado("activo");
                boletoDAO.create(boleto);

                // Actualizar estado del asiento
                asientoDAO.updateEstado(eventoId, numeroAsiento, "vendido");
            }

            // Crear reserva
            Reserva reserva = new Reserva(eventoId, usuarioId, asientos.size(), totalPagado);
            reserva.setFechaReserva(LocalDateTime.now());
            reserva.setEstado("activa");
            reservaDAO.create(reserva);

            // Enviar email
            Usuario usuario = usuarioDAO.read(usuarioId);
            if (usuario != null) {
                emailService.enviarConfirmacionCompra(usuario, evento, asientos, totalPagado);
            }

            return reserva;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Boleto> obtenerBoletosUsuario(int usuarioId) throws SQLException {
        return boletoDAO.findByUsuario(usuarioId);
    }

    public List<Reserva> obtenerReservasUsuario(int usuarioId) throws SQLException {
        return reservaDAO.findByUsuario(usuarioId);
    }

    public Boleto obtenerBoletoPorCodigo(String codigo) throws SQLException {
        return boletoDAO.findByCodigoBoleto(codigo);
    }

    public boolean cancelarReserva(int reservaId) throws SQLException {
        try {
            Reserva reserva = reservaDAO.read(reservaId);
            if (reserva != null && reserva.getEstado().equals("activa")) {
                reserva.setEstado("cancelada");
                reservaDAO.update(reserva);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalVentasEvento(int eventoId) throws SQLException {
        return boletoDAO.getTotalVentasByEvento(eventoId);
    }
}
