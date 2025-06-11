package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.FacturaService;
import TFG.Terranaturale.model.Dto.FacturaDTO;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> getAllFacturas() {
        logger.info("GET request to get all facturas");
        List<FacturaDTO> facturas = facturaService.findAll();
        logger.info("Returning {} facturas", facturas.size());
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable Integer id) {
        logger.info("GET request to get factura with id: {}", id);
        try {
            FacturaDTO facturaDTO = facturaService.findById(id);
            logger.info("Returning factura with id: {}", id);
            return ResponseEntity.ok(facturaDTO);
        } catch (Exception e) {
            logger.warn("Factura with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FacturaDTO> createFactura(@RequestBody FacturaDTO facturaDTO) {
        logger.info("POST request to create factura");
        FacturaDTO createdFactura = facturaService.save(facturaDTO);
        logger.info("Created factura with id: {}", createdFactura.getId());
        return ResponseEntity.ok(createdFactura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> updateFactura(@PathVariable Integer id, @RequestBody FacturaDTO facturaDTO) {
        logger.info("PUT request to update factura with id: {}", id);
        try {
            facturaDTO.setId(id);
            FacturaDTO updatedFactura = facturaService.update(id, facturaDTO);
            logger.info("Updated factura with id: {}", updatedFactura.getId());
            return ResponseEntity.ok(updatedFactura);
        } catch (Exception e) {
            logger.warn("Factura with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Integer id) {
        logger.info("DELETE request to delete factura with id: {}", id);
        try {
            facturaService.deleteById(id);
            logger.info("Deleted factura with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warn("Factura with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/client")
    public ResponseEntity<List<FacturaDTO>> getPresupuestoByClientId(@RequestBody UsuarioDTO id) {
        logger.info("POST request to get facturas by client id");
        ResponseEntity<List<FacturaDTO>> response = facturaService.findByClient(id);
        logger.info("Returning {} facturas for client", response.getBody().size());
        return response;
    }

//    @GetMapping("/douwnload/{id}")
//    public ResponseEntity<InputStreamResource> downloadPresupuesto(@PathVariable Integer id) throws IOException {
//        logger.info("GET request to download factura with id: {}", id);
//        return facturaService.downloadFactura(id);
//    }
}
