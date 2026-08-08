package beans;

import services.ReportService;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

@Named(value = "reportesBean")
@SessionScoped
public class ReportesBean implements Serializable {
    private ReportService reportService;
    private double totalVentasGlobal;
    private int totalBoletosVendidos;
    private Map<String, Integer> asistenciaPorCategoria;
    private Map<String, Double> ventasPorCategoria;

    public ReportesBean() {
        this.reportService = new ReportService();
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            totalVentasGlobal = reportService.getTotalVentasGlobal();
            totalBoletosVendidos = reportService.getTotalBoletosVendidos();
            asistenciaPorCategoria = reportService.getAsistenciaPorCategoria();
            ventasPorCategoria = reportService.getVentasPorCategoria();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exportarPDF() {
        // Implementar exportación a PDF con OpenPDF
        System.out.println("Exportando a PDF...");
    }

    public void exportarExcel() {
        // Implementar exportación a Excel con POI
        System.out.println("Exportando a Excel...");
    }

    // Getters y Setters
    public double getTotalVentasGlobal() {
        return totalVentasGlobal;
    }

    public void setTotalVentasGlobal(double totalVentasGlobal) {
        this.totalVentasGlobal = totalVentasGlobal;
    }

    public int getTotalBoletosVendidos() {
        return totalBoletosVendidos;
    }

    public void setTotalBoletosVendidos(int totalBoletosVendidos) {
        this.totalBoletosVendidos = totalBoletosVendidos;
    }

    public Map<String, Integer> getAsistenciaPorCategoria() {
        return asistenciaPorCategoria;
    }

    public void setAsistenciaPorCategoria(Map<String, Integer> asistenciaPorCategoria) {
        this.asistenciaPorCategoria = asistenciaPorCategoria;
    }

    public Map<String, Double> getVentasPorCategoria() {
        return ventasPorCategoria;
    }

    public void setVentasPorCategoria(Map<String, Double> ventasPorCategoria) {
        this.ventasPorCategoria = ventasPorCategoria;
    }
}
