package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for generating reports for budgets and invoices
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Generates a PDF report for a budget
     * @param presupuestoId ID of the budget to generate the report for
     * @return ResponseEntity containing the PDF report
     */
    @GetMapping("/presupuestos/{presupuestoId}")
    public ResponseEntity<byte[]> generatePresupuestoPdf(@PathVariable Integer presupuestoId) {
        logger.info("GET request to generate PDF report for budget with ID: {}", presupuestoId);
        return reportService.generatePresupuestoPdf(presupuestoId);
    }

    /**
     * Generates a PDF report for an invoice
     * @param facturaId ID of the invoice to generate the report for
     * @return ResponseEntity containing the PDF report
     */
    @GetMapping("/facturas/{facturaId}")
    public ResponseEntity<byte[]> generateFacturaPdf(@PathVariable Integer facturaId) {
        logger.info("GET request to generate PDF report for invoice with ID: {}", facturaId);
        return reportService.generateFacturaPdf(facturaId);
    }
}