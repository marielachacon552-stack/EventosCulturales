package utils;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import data.Evento;
import database.EventoDAO;
import java.sql.SQLException;

@FacesConverter("eventoConverter")
public class EventoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            EventoDAO dao = new EventoDAO();
            return dao.read(Integer.parseInt(value));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Evento) {
            return String.valueOf(((Evento) value).getId());
        }
        return value.toString();
    }
}
