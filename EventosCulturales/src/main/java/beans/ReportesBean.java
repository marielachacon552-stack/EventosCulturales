package beans;

import services.ReportService;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
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
        this.asistenciaPorCategoria = new HashMap<>();
        this.ventasPorCategoria = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        try {
            totalVentasGlobal = reportService.getTotalVentasGlobal();
            totalBoletosVendidos = reportService.getTotalBoletosVendidos();

            Map<String, Integer> asistencia = reportService.getAsistenciaPorCategoria();
            if (asistencia != null) {
                this.asistenciaPorCategoria = asistencia;
            } else {
                this.asistenciaPorCategoria.clear();
            }

            Map<String, Double> ventas = reportService.getVentasPorCategoria();
            if (ventas != null) {
                this.ventasPorCategoria = ventas;
            } else {
                this.ventasPorCategoria.clear();
            }

        } catch (SQLException e) {
            System.out.println("ERROR al cargar datos en ReportesBean: " + e.getMessage());
            e.printStackTrace();
        }
    }



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