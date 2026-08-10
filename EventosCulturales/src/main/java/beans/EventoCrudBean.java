package beans;

import data.Evento;
import services.EventService;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Named(value = "eventoCrudBean")
@SessionScoped
public class EventoCrudBean implements Serializable {
    private EventService eventService;
    private List<Evento> eventos;
    private Evento eventoSeleccionado;
    private boolean mostrarDialogo;

    public EventoCrudBean() {
        this.eventService = new EventService();
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
        // Inicializamos un objeto completamente nuevo con ID 0 para creación
        this.eventoSeleccionado = new Evento();
        this.mostrarDialogo = true;
    }

    public void abrirDialogoEditar() {
        // Si ya viene seleccionado de la tabla, se queda listo para editar
        if (this.eventoSeleccionado != null) {
            this.mostrarDialogo = true;
        }
    }

    public void guardar() {
        if (!eventService.validarEvento(eventoSeleccionado)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Validación", "Por favor complete todos los campos correctamente"));
            return;
        }

        try {
            if (eventoSeleccionado.getId() == 0) {
                // Crear nuevo
                if (eventService.crearEvento(eventoSeleccionado)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Evento creado correctamente"));
                    this.mostrarDialogo = false;
                    cargarEventos();
                }
            } else {
                // Actualizar
                if (eventService.actualizarEvento(eventoSeleccionado)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Evento actualizado correctamente"));
                    this.mostrarDialogo = false;
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
        this.mostrarDialogo = false;
        this.eventoSeleccionado = new Evento();
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

    public boolean isMostrarDialogo() {
        return mostrarDialogo;
    }

    public void setMostrarDialogo(boolean mostrarDialogo) {
        this.mostrarDialogo = mostrarDialogo;
    }
}