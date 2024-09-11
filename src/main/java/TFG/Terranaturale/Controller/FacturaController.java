package TFG.Terranaturale.Controller;

import Dto.FacturaDTO;

import Dto.FileUpload;
import Dto.UsuarioDTO;
import TFG.Terranaturale.Service.FacturaService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final  FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> getAllFacturas() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> getFacturaById(@PathVariable Integer id) {
        FacturaDTO facturaDTO = facturaService.findById(id);
        if (facturaDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facturaDTO);
    }

    @PostMapping
    public ResponseEntity<FacturaDTO> createFactura(@RequestBody FacturaDTO facturaDTO) {
        FacturaDTO createdFactura = facturaService.save(facturaDTO);
        return ResponseEntity.ok(createdFactura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaDTO> updateFactura(@PathVariable Integer id, @RequestBody FacturaDTO facturaDTO) {
        FacturaDTO existingFactura = facturaService.findById(id);
        if (existingFactura == null) {
            return ResponseEntity.notFound().build();
        }
        facturaDTO.setId(id);
        FacturaDTO updatedFactura = facturaService.save(facturaDTO);
        return ResponseEntity.ok(updatedFactura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Integer id) {
        FacturaDTO existingFactura = facturaService.findById(id);
        if (existingFactura == null) {
            return ResponseEntity.notFound().build();
        }
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/client")
    public ResponseEntity<List<FacturaDTO>> getPresupuestoByClientId(@RequestBody UsuarioDTO id) {
        return facturaService.findByClient(id);
    }

    @GetMapping("/douwnload/{id}")
    public ResponseEntity<InputStreamResource> downloadPresupuesto(@PathVariable Integer id) throws IOException {
        return facturaService.downloadFactura(id);
    }

    @PostMapping("/upload")
    public void uploadImagene(@RequestBody FileUpload imagenDTO) {
        facturaService.uploadFile(imagenDTO);
    }
}
