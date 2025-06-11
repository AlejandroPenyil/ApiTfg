package TFG.Terranaturale.Service;

import TFG.Terranaturale.Repository.DetalleFacturaRepository;
import TFG.Terranaturale.Repository.DetallePresupuestoRepository;
import TFG.Terranaturale.Repository.FacturaRepository;
import TFG.Terranaturale.Repository.PresupuestoRepository;
import TFG.Terranaturale.model.Entity.DetalleFactura;
import TFG.Terranaturale.model.Entity.DetallePresupuesto;
import TFG.Terranaturale.model.Entity.Factura;
import TFG.Terranaturale.model.Entity.Presupuesto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Service for generating reports for budgets and invoices
 * This is a placeholder implementation that will be enhanced with JasperReports
 * when the dependency is properly resolved.
 */
@Service
public class ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final PresupuestoRepository presupuestoRepository;
    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final DetallePresupuestoRepository detallePresupuestoRepository;

    public ReportService(PresupuestoRepository presupuestoRepository, 
                        FacturaRepository facturaRepository,
                        DetalleFacturaRepository detalleFacturaRepository,
                        DetallePresupuestoRepository detallePresupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.detallePresupuestoRepository = detallePresupuestoRepository;
    }

    /**
     * Prepares a response entity with a PDF report for a budget
     * @param presupuestoId ID of the budget to generate the report for
     * @return ResponseEntity containing the PDF report
     */
    public ResponseEntity<byte[]> generatePresupuestoPdf(Integer presupuestoId) {
        logger.info("Generating PDF report for budget with ID: {}", presupuestoId);

        Optional<Presupuesto> presupuestoOpt = presupuestoRepository.findById(presupuestoId);
        if (!presupuestoOpt.isPresent()) {
            logger.error("Budget with ID {} not found", presupuestoId);
            throw new RuntimeException("Budget not found");
        }

        Presupuesto presupuesto = presupuestoOpt.get();

        // This is a placeholder implementation
        // In a real implementation, this would use JasperReports to generate a PDF
        // The template file would be loaded from /reports/presupuesto_template.jrxml
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("PRESUPUESTO\n\n");
        reportContent.append("Número de Presupuesto: ").append(presupuesto.getNumeroPresupuesto()).append("\n");
        reportContent.append("Fecha: ").append(presupuesto.getFechalEnvio()).append("\n");
        reportContent.append("Validez: ").append(presupuesto.getValidez()).append(" días\n");
        reportContent.append("Estado: ").append(presupuesto.getEstado()).append("\n\n");

        reportContent.append("DATOS DEL CLIENTE\n");
        reportContent.append("Cliente: ").append(presupuesto.getIdJardin().getIdCliente().getNombre()).append(" ")
                .append(presupuesto.getIdJardin().getIdCliente().getApellidos()).append("\n");
        reportContent.append("DNI/CIF: ").append(presupuesto.getIdJardin().getIdCliente().getDni()).append("\n");
        reportContent.append("Dirección: ").append(presupuesto.getIdJardin().getIdCliente().getDireccion()).append("\n\n");

        reportContent.append("DATOS DEL JARDÍN\n");
        reportContent.append("Localización: ").append(presupuesto.getIdJardin().getLocalizacion()).append("\n");
        reportContent.append("Tamaño: ").append(presupuesto.getIdJardin().getTamaño()).append(" m²\n\n");

        reportContent.append("DETALLES DEL PRESUPUESTO\n");
        reportContent.append("------------------------------------------------------------------\n");
        reportContent.append("Concepto                  Cantidad    Precio    Subtotal\n");
        reportContent.append("------------------------------------------------------------------\n");

        // Add budget details
        List<DetallePresupuesto> detalles = detallePresupuestoRepository.findByIdPresupuesto(presupuesto);
        for (DetallePresupuesto detalle : detalles) {
            String concepto = detalle.getDescripcion() != null ? detalle.getDescripcion() : detalle.getIdCosto().getName();
            reportContent.append(String.format("%-25s %-10s %-10s %-10s\n", 
                concepto,
                detalle.getCantidad().toString(),
                detalle.getPrecioUnitario().toString() + "€",
                detalle.getSubtotal().toString() + "€"));
        }

        reportContent.append("------------------------------------------------------------------\n\n");
        reportContent.append(String.format("%-46s %-10s\n", "Subtotal:", presupuesto.getSubtotal() + "€"));
        reportContent.append(String.format("%-46s %-10s\n", "IVA (21%):", presupuesto.getIva() + "€"));

        if (presupuesto.getDescuento() != null && presupuesto.getDescuento().compareTo(BigDecimal.ZERO) > 0) {
            reportContent.append(String.format("%-46s %-10s\n", "Descuento:", presupuesto.getDescuento() + "€"));
        }

        reportContent.append(String.format("%-46s %-10s\n", "TOTAL:", presupuesto.getTotal() + "€"));

        if (presupuesto.getNotas() != null && !presupuesto.getNotas().isEmpty()) {
            reportContent.append("\nNotas: ").append(presupuesto.getNotas()).append("\n");
        }

        byte[] reportBytes = reportContent.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "presupuesto_" + presupuesto.getNumeroPresupuesto() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportBytes);
    }

    /**
     * Prepares a response entity with a PDF report for an invoice
     * @param facturaId ID of the invoice to generate the report for
     * @return ResponseEntity containing the PDF report
     */
    public ResponseEntity<byte[]> generateFacturaPdf(Integer facturaId) {
        logger.info("Generating PDF report for invoice with ID: {}", facturaId);

        Optional<Factura> facturaOpt = facturaRepository.findById(facturaId);
        if (!facturaOpt.isPresent()) {
            logger.error("Invoice with ID {} not found", facturaId);
            throw new RuntimeException("Invoice not found");
        }

        Factura factura = facturaOpt.get();

        // This is a placeholder implementation
        // In a real implementation, this would use JasperReports to generate a PDF
        // The template file would be loaded from /reports/factura_template.jrxml
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("FACTURA\n\n");
        reportContent.append("Número de Factura: ").append(factura.getNumeroFactura()).append("\n");
        reportContent.append("Fecha: ").append(factura.getFecha()).append("\n\n");

        reportContent.append("DATOS DEL CLIENTE\n");
        reportContent.append("Cliente: ").append(factura.getIdCliente().getNombre()).append(" ").append(factura.getIdCliente().getApellidos()).append("\n");
        reportContent.append("DNI/CIF: ").append(factura.getIdCliente().getDni()).append("\n");
        reportContent.append("Dirección: ").append(factura.getIdCliente().getDireccion()).append("\n\n");

        reportContent.append("DETALLES DE LA FACTURA\n");
        reportContent.append("------------------------------------------------------------------\n");
        reportContent.append("Concepto                  Cantidad    Precio    Subtotal\n");
        reportContent.append("------------------------------------------------------------------\n");

        // Add invoice details
        List<DetalleFactura> detalles = detalleFacturaRepository.findByIdFactura(factura);
        for (DetalleFactura detalle : detalles) {
            String concepto = detalle.getDescripcion() != null ? detalle.getDescripcion() : detalle.getIdCosto().getName();
            reportContent.append(String.format("%-25s %-10s %-10s %-10s\n", 
                concepto,
                detalle.getCantidad().toString(),
                detalle.getPrecioUnitario().toString() + "€",
                detalle.getSubtotal().toString() + "€"));
        }

        reportContent.append("------------------------------------------------------------------\n\n");
        reportContent.append(String.format("%-46s %-10s\n", "Subtotal:", factura.getSubtotal() + "€"));
        reportContent.append(String.format("%-46s %-10s\n", "IVA (21%):", factura.getIva() + "€"));

        if (factura.getDescuento() != null && factura.getDescuento().compareTo(BigDecimal.ZERO) > 0) {
            reportContent.append(String.format("%-46s %-10s\n", "Descuento:", factura.getDescuento() + "€"));
        }

        reportContent.append(String.format("%-46s %-10s\n", "TOTAL:", factura.getTotal() + "€"));

        if (factura.getNotas() != null && !factura.getNotas().isEmpty()) {
            reportContent.append("\nNotas: ").append(factura.getNotas()).append("\n");
        }

        byte[] reportBytes = reportContent.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "factura_" + factura.getNumeroFactura() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportBytes);
    }
}
