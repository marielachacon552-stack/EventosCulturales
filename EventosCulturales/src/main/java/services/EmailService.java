package services;

import data.Evento;
import data.Usuario;
import jakarta.faces.context.FacesContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailService {

    public void enviarConfirmacionCompra(Usuario usuario, Evento evento, List<String> asientos, double total) {
        try {
            String contenido = generarConfirmacionCompra(usuario, evento, asientos, total);
            guardarEnArchivo(usuario.getCorreo(), contenido);
            System.out.println("✓ Confirmación enviada a: " + usuario.getCorreo());
        } catch (IOException e) {
            System.err.println("❌ ERROR AL GUARDAR EL ARCHIVO DE CORREO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generarConfirmacionCompra(Usuario usuario, Evento evento, List<String> asientos, double total) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();

        sb.append("=================================================\n");
        sb.append("CONFIRMACIÓN DE COMPRA - EVENTOS CULTURALES\n");
        sb.append("=================================================\n\n");

        sb.append("DATOS DEL CLIENTE:\n");
        sb.append("Nombre: ").append(usuario.getNombre()).append("\n");
        sb.append("Correo: ").append(usuario.getCorreo()).append("\n\n");

        sb.append("INFORMACIÓN DEL EVENTO:\n");
        sb.append("Evento: ").append(evento.getTitulo()).append("\n");
        sb.append("Categoría: ").append(evento.getCategoria()).append("\n");
        sb.append("Fecha: ").append(evento.getFechaHora().format(formatter)).append("\n");
        sb.append("Duración: ").append(evento.getDuracion()).append(" minutos\n");
        sb.append("Ubicación: ").append(evento.getUbicacion()).append("\n\n");

        sb.append("ASIENTOS RESERVADOS:\n");
        for (String asiento : asientos) {
            sb.append("  - ").append(asiento).append("\n");
        }

        sb.append("\nDETALLES DE LA COMPRA:\n");
        sb.append("Cantidad de boletos: ").append(asientos.size()).append("\n");
        sb.append("Precio por boleto: L.").append(String.format("%.2f", evento.getPrecioBoleto())).append("\n");
        sb.append("TOTAL A PAGAR: L.").append(String.format("%.2f", total)).append("\n\n");

        sb.append("Gracias por su compra. ¡Disfrute del evento!\n");
        sb.append("=================================================\n");

        return sb.toString();
    }

    private void guardarEnArchivo(String correo, String contenido) throws IOException {
        String path = null;

        try {
            path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/emails/");
        } catch (Exception e) {

        }


        if (path == null) {
            path = System.getProperty("user.home") + "/Desktop/emails/";
        }

        File directorio = new File(path);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        String fileName = path + File.separator + correo.replace("@", "_at_") + "_" + System.currentTimeMillis() + ".txt";

        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(contenido);
        }

        System.out.println("Archivo de confirmación guardado en: " + fileName);
    }
}