package services;

import data.Evento;
import data.Usuario;
import database.EventoDAO;
import database.BoletoDAO;
import database.ReservaDAO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private EventoDAO eventoDAO;
    private BoletoDAO boletoDAO;
    private ReservaDAO reservaDAO;

    public ReportService() {
        this.eventoDAO = new EventoDAO();
        this.boletoDAO = new BoletoDAO();
        this.reservaDAO = new ReservaDAO();
    }

    public double getTotalVentasGlobal() throws SQLException {
        List<Evento> eventos = eventoDAO.readAll();
        double total = 0;
        for (Evento evento : eventos) {
            total += boletoDAO.getTotalVentasByEvento(evento.getId());
        }
        return total;
    }

    public int getTotalBoletosVendidos() throws SQLException {
        List<Evento> eventos = eventoDAO.readAll();
        int total = 0;
        for (Evento evento : eventos) {
            total += boletoDAO.countByEvento(evento.getId());
        }
        return total;
    }

    public Map<String, Integer> getAsistenciaPorCategoria() throws SQLException {
        Map<String, Integer> asistencia = new HashMap<>();
        List<Evento> eventos = eventoDAO.readAll();

        for (Evento evento : eventos) {
            int count = boletoDAO.countByEvento(evento.getId());
            String categoria = evento.getCategoria();
            asistencia.put(categoria, asistencia.getOrDefault(categoria, 0) + count);
        }

        return asistencia;
    }

    public Map<String, Double> getVentasPorCategoria() throws SQLException {
        Map<String, Double> ventas = new HashMap<>();
        List<Evento> eventos = eventoDAO.readAll();

        for (Evento evento : eventos) {
            double venta = boletoDAO.getTotalVentasByEvento(evento.getId());
            String categoria = evento.getCategoria();
            ventas.put(categoria, ventas.getOrDefault(categoria, 0.0) + venta);
        }

        return ventas;
    }

    public Map<String, Object> getReporteEvento(int eventoId) throws SQLException {
        Map<String, Object> reporte = new HashMap<>();
        Evento evento = eventoDAO.read(eventoId);

        if (evento != null) {
            reporte.put("evento", evento);
            reporte.put("boletosVendidos", boletoDAO.countByEvento(eventoId));
            reporte.put("totalVentas", boletoDAO.getTotalVentasByEvento(eventoId));
            reporte.put("asientosDisponibles", eventoDAO.read(eventoId).getAforoMaximo() - boletoDAO.countByEvento(eventoId));
        }

        return reporte;
    }

    public List<Evento> obtenerEventosPorAsistencia() throws SQLException {
        List<Evento> eventos = eventoDAO.readAll();
        // Ordenar por cantidad de boletos vendidos (simplificado)
        return eventos;
    }
}
