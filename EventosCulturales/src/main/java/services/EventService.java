package services;

import data.Evento;
import database.EventoDAO;
import database.AsientoDAO;
import java.sql.SQLException;
import java.util.List;

public class EventService {
    private EventoDAO eventoDAO;
    private AsientoDAO asientoDAO;

    public EventService() {
        this.eventoDAO = new EventoDAO();
        this.asientoDAO = new AsientoDAO();
    }

    public boolean crearEvento(Evento evento) throws SQLException {
        try {
            eventoDAO.create(evento);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarEvento(Evento evento) throws SQLException {
        try {
            eventoDAO.update(evento);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarEvento(int eventoId) throws SQLException {
        try {
            eventoDAO.delete(eventoId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Evento obtenerEvento(int eventoId) throws SQLException {
        return eventoDAO.read(eventoId);
    }

    public List<Evento> obtenerTodosEventos() throws SQLException {
        return eventoDAO.readAll();
    }

    public List<Evento> obtenerEventosPorCategoria(String categoria) throws SQLException {
        return eventoDAO.findByCategoria(categoria);
    }

    public List<Evento> obtenerEventosProximos() throws SQLException {
        return eventoDAO.findProximos();
    }

    public int obtenerAsientosDisponibles(int eventoId) throws SQLException {
        return asientoDAO.countDisponiblesByEvento(eventoId);
    }

    public boolean validarEvento(Evento evento) {
        if (evento.getTitulo() == null || evento.getTitulo().trim().isEmpty()) {
            return false;
        }
        if (evento.getCategoria() == null || evento.getCategoria().trim().isEmpty()) {
            return false;
        }
        if (evento.getFechaHora() == null) {
            return false;
        }
        if (evento.getDuracion() <= 0) {
            return false;
        }
        if (evento.getPrecioBoleto() < 0) {
            return false;
        }
        if (evento.getAforoMaximo() <= 0) {
            return false;
        }
        if (evento.getUbicacion() == null || evento.getUbicacion().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}