package utils;

import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import data.Usuario;

public class SecurityPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        String requestPath = context.getExternalContext().getRequestServletPath();

        // Páginas que no requieren autenticación
        if (requestPath.contains("login.xhtml") || requestPath.contains("index.xhtml")) {
            return;
        }

        // Verificar si el usuario está autenticado
        Usuario usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuarioActual");
        if (usuario == null) {
            // Redirigir al login
            NavigationHandler nh = context.getApplication().getNavigationHandler();
            nh.handleNavigation(context, null, "redirect:login.xhtml");
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        // No implementado
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
