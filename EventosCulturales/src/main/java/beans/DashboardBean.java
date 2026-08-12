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
import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    private Map<String, Integer> categoriasMap = new HashMap<>();

    public DashboardBean() {
        this.eventoDAO = new EventoDAO();
        this.boletoDAO = new BoletoDAO();
        this.reservaDAO = new ReservaDAO();
        this.asientoDAO = new AsientoDAO();
    }

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            eventosProximos = eventoDAO.findProximos();

            totalBoletosVendidos = 0;
            totalIngresos = 0.0;
            categoriasMap.clear();

            List<Evento> todosEventos = eventoDAO.readAll();
            if (todosEventos != null) {
                for (Evento evento : todosEventos) {
                    int boletos = boletoDAO.countByEvento(evento.getId());
                    double ventas = boletoDAO.getTotalVentasByEvento(evento.getId());

                    totalBoletosVendidos += boletos;
                    totalIngresos += ventas;

                    String cat = (evento.getCategoria() != null && !evento.getCategoria().isEmpty())
                            ? evento.getCategoria() : "Sin Categoría";
                    categoriasMap.put(cat, categoriasMap.getOrDefault(cat, 0) + 1);
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR al cargar datos del dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void verDetalles(Evento evento) {
        this.eventoDetalles = evento;
    }


    public Map<String, Integer> getCategoriasMap() {
        return categoriasMap;
    }

    public Usuario getUsuarioActual() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioActual");
    }

    public int getRoleId() {
        Integer roleId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("roleId");
        return roleId != null ? roleId : 0;
    }

    public boolean esAdmin() { return getRoleId() == 1; }
    public boolean esOrganizador() { return getRoleId() == 2; }
    public boolean esCliente() { return getRoleId() == 3; }

    public List<Evento> getEventosProximos() { return eventosProximos; }
    public void setEventosProximos(List<Evento> eventosProximos) { this.eventosProximos = eventosProximos; }

    public Evento getEventoDetalles() { return eventoDetalles; }
    public void setEventoDetalles(Evento eventoDetalles) { this.eventoDetalles = eventoDetalles; }

    public int getTotalBoletosVendidos() { return totalBoletosVendidos; }
    public void setTotalBoletosVendidos(int totalBoletosVendidos) { this.totalBoletosVendidos = totalBoletosVendidos; }

    public double getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(double totalIngresos) { this.totalIngresos = totalIngresos; }
}