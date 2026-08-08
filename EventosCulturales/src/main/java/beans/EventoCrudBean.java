package beans;

import data.Evento;
import services.EventService;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Named(value = "eventoCrudBean")
@SessionScoped
public class EventoCrudBean implements Serializable {
    private EventService eventService;
    private List<Evento> eventos;
    private Evento eventoSeleccionado;
    private Evento eventoNuevo;
    private boolean mostrarDialogo;

    public EventoCrudBean() {
        this.eventService = new EventService();
        this.eventoNuevo = new Evento();
        cargarEventos();
    }

    public void cargarEventos() {
        try {
            eventos = eventService.obtenerTodosEventos();
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al cargar eventos"));
        }
    }

    public void abrirDialogoNuevo() {
        eventoNuevo = new Evento();
        mostrarDialogo = true;
    }

    public void abrirDialogoEditar() {
        if (eventoSeleccionado != null) {
            eventoNuevo = new Evento();
            eventoNuevo.setId(eventoSeleccionado.getId());
            eventoNuevo.setTitulo(eventoSeleccionado.getTitulo());
            eventoNuevo.setCategoria(eventoSeleccionado.getCategoria());
            eventoNuevo.setFechaHora(eventoSeleccionado.getFechaHora());
            eventoNuevo.setDuracion(eventoSeleccionado.getDuracion());
            eventoNuevo.setDescripcion(eventoSeleccionado.getDescripcion());
            eventoNuevo.setPrecioBoieto(eventoSeleccionado.getPrecioBoieto());
            eventoNuevo.setAforoMaximo(eventoSeleccionado.getAforoMaximo());
            eventoNuevo.setUbicacion(eventoSeleccionado.getUbicacion());
            mostrarDialogo = true;
        }
    }

    public void guardar() {
        if (!eventService.validarEvento(eventoNuevo)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Validación", "Por favor complete todos los campos correctamente"));
            return;
        }

        try {
            if (eventoNuevo.getId() == 0) {
                // Crear nuevo
                if (eventService.crearEvento(eventoNuevo)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Evento creado correctamente"));
                    mostrarDialogo = false;
                    cargarEventos();
                }
            } else {
                // Actualizar
                if (eventService.actualizarEvento(eventoNuevo)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Evento actualizado correctamente"));
                    mostrarDialogo = false;
                    cargarEventos();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar evento"));
        }
    }

    public void eliminar() {
        if (eventoSeleccionado != null) {
            try {
                if (eventService.eliminarEvento(eventoSeleccionado.getId())) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Evento eliminado correctamente"));
                    cargarEventos();
                    eventoSeleccionado = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar evento"));
            }
        }
    }

    public void cancelar() {
        mostrarDialogo = false;
        eventoNuevo = new Evento();
    }

    // Getters y Setters
    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Evento getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    public void setEventoSeleccionado(Evento eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public Evento getEventoNuevo() {
        return eventoNuevo;
    }

    public void setEventoNuevo(Evento eventoNuevo) {
        this.eventoNuevo = eventoNuevo;
    }

    public boolean isMostrarDialogo() {
        return mostrarDialogo;
    }

    public void setMostrarDialogo(boolean mostrarDialogo) {
        this.mostrarDialogo = mostrarDialogo;
    }
}
