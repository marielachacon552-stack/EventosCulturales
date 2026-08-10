package beans;

import data.Usuario;
import data.Evento;
import database.EventoDAO;
import database.BoletoDAO;
import database.ReservaDAO;
import database.AsientoDAO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Named(value = "dashboardBean")
@SessionScoped
public class DashboardBean implements Serializable {
    private EventoDAO eventoDAO;
    private BoletoDAO boletoDAO;
    private ReservaDAO reservaDAO;
    private AsientoDAO asientoDAO;
    private List<Evento> eventosProximos;
    private Evento eventoDetalles;
    private int totalBoletosVendidos;
    private double totalIngresos;

    public DashboardBean() {
        this.eventoDAO = new EventoDAO();
        this.boletoDAO = new BoletoDAO();
        this.reservaDAO = new ReservaDAO();
        this.asientoDAO = new AsientoDAO();
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            eventosProximos = eventoDAO.findProximos();
            
            totalBoletosVendidos = 0;
            totalIngresos = 0;
            
            for (Evento evento : eventoDAO.readAll()) {
                totalBoletosVendidos += boletoDAO.countByEvento(evento.getId());
                totalIngresos += boletoDAO.getTotalVentasByEvento(evento.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void verDetalles(Evento evento) {
        this.eventoDetalles = evento;
    }

    public Usuario getUsuarioActual() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioActual");
    }

    public int getRoleId() {
        Integer roleId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("roleId");
        return roleId != null ? roleId : 0;
    }

    public boolean esAdmin() {
        return getRoleId() == 1;
    }

    public boolean esOrganizador() {
        return getRoleId() == 2;
    }

    public boolean esCliente() {
        return getRoleId() == 3;
    }

    public List<Evento> getEventosProximos() {
        return eventosProximos;
    }

    public void setEventosProximos(List<Evento> eventosProximos) {
        this.eventosProximos = eventosProximos;
    }

    public Evento getEventoDetalles() {
        return eventoDetalles;
    }

    public void setEventoDetalles(Evento eventoDetalles) {
        this.eventoDetalles = eventoDetalles;
    }

    public int getTotalBoletosVendidos() {
        return totalBoletosVendidos;
    }

    public void setTotalBoletosVendidos(int totalBoletosVendidos) {
        this.totalBoletosVendidos = totalBoletosVendidos;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }
}
