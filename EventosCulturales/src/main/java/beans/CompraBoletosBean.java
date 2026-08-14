package beans;

import data.Evento;
import data.Asiento;
import data.Boleto;
import data.Reserva;
import data.Usuario;
import database.EventoDAO;
import database.UsuarioDAO;
import database.AsientoDAO;
import database.BoletoDAO;
import services.CompraService;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Named(value = "compraBoletosBean")
@ViewScoped
public class CompraBoletosBean implements Serializable {
    private EventoDAO eventoDAO;
    private AsientoDAO asientoDAO;
    private UsuarioDAO usuarioDAO;
    private BoletoDAO boletoDAO;
    private CompraService compraService;

    private List<Evento> eventos;
    private Evento eventoSeleccionado;
    private List<Asiento> asientosDisponibles;
    private List<String> asientosSeleccionados;
    private List<Boleto> misboletos;
    private List<Reserva> misReservas;
    private double totalPago;

    public CompraBoletosBean() {
        this.eventoDAO = new EventoDAO();
        this.asientoDAO = new AsientoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.boletoDAO = new BoletoDAO();
        this.compraService = new CompraService();
        this.asientosSeleccionados = new ArrayList<>();
        cargarEventos();
        cargarBoletosYReservas();
    }

    public void cancelarBoleto(Boleto boleto) {
        try {

            boletoDAO.updateEstado(boleto.getId(), "cancelado");

            asientoDAO.updateEstado(boleto.getEventoId(), boleto.getNumeroAsiento(), "disponible");

            cargarBoletosYReservas();
            if (eventoSeleccionado != null && eventoSeleccionado.getId() == boleto.getEventoId()) {
                seleccionarEvento();
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "El boleto ha sido cancelado y el asiento liberado correctamente."));

        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cancelar el boleto: " + e.getMessage()));
        }
    }

    public void cargarEventos() {
        try {
            eventos = eventoDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void seleccionarEvento() {
        if (eventoSeleccionado != null) {
            try {
                asientosDisponibles = asientoDAO.findDisponiblesByEvento(eventoSeleccionado.getId());
                asientosSeleccionados.clear();
                totalPago = 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actualizarTotal() {
        if (eventoSeleccionado != null) {
            totalPago = asientosSeleccionados.size() * eventoSeleccionado.getPrecioBoleto();
        }
    }

    public void procesarCompra() {
        if (asientosSeleccionados.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Validación", "Seleccione al menos un asiento"));
            return;
        }

        try {
            Integer usuarioId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioId");

            Reserva reserva = compraService.procesarCompra(usuarioId, eventoSeleccionado.getId(), asientosSeleccionados, usuarioDAO);

            if (reserva != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "¡Compra realizada! Total: L." + String.format("%.2f", totalPago)));

                asientosSeleccionados.clear();
                totalPago = 0;
                seleccionarEvento();
                cargarBoletosYReservas();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Los asientos seleccionados no están disponibles"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al procesar la compra"));
        }
    }

    public void cargarBoletosYReservas() {
        try {
            Integer usuarioId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioId");
            if (usuarioId != null) {
                misboletos = compraService.obtenerBoletosUsuario(usuarioId);
                misReservas = compraService.obtenerReservasUsuario(usuarioId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCountAsientosDisponibles() {
        if (eventoSeleccionado != null) {
            try {
                return asientoDAO.countDisponiblesByEvento(eventoSeleccionado.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public List<Evento> getEventos() { return eventos; }
    public void setEventos(List<Evento> eventos) { this.eventos = eventos; }
    public Evento getEventoSeleccionado() { return eventoSeleccionado; }
    public void setEventoSeleccionado(Evento eventoSeleccionado) { this.eventoSeleccionado = eventoSeleccionado; }
    public List<Asiento> getAsientosDisponibles() { return asientosDisponibles; }
    public void setAsientosDisponibles(List<Asiento> asientosDisponibles) { this.asientosDisponibles = asientosDisponibles; }
    public List<String> getAsientosSeleccionados() { return asientosSeleccionados; }
    public void setAsientosSeleccionados(List<String> asientosSeleccionados) { this.asientosSeleccionados = asientosSeleccionados; }
    public double getTotalPago() { return totalPago; }
    public void setTotalPago(double totalPago) { this.totalPago = totalPago; }
    public List<Boleto> getMisboletos() { return misboletos; }
    public void setMisboletos(List<Boleto> misboletos) { this.misboletos = misboletos; }
    public List<Reserva> getMisReservas() { return misReservas; }
    public void setMisReservas(List<Reserva> misReservas) { this.misReservas = misReservas; }
}