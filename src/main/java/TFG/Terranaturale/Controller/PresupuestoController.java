package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.PresupuestoService;
import TFG.Terranaturale.model.Dto.FileUpload;
import TFG.Terranaturale.model.Dto.PresupuestoDTO;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presupuestos")
public class PresupuestoController {
    private static final Logger logger = LoggerFactory.getLogger(PresupuestoController.class);

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @GetMapping
    public ResponseEntity<List<PresupuestoDTO>> getAllPresupuestos() {
        logger.info("GET request to get all presupuestos");
        List<PresupuestoDTO> presupuestos = presupuestoService.findAll();
        logger.info("Returning {} presupuestos", presupuestos.size());
        return ResponseEntity.ok(presupuestos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoDTO> getPresupuestoById(@PathVariable Integer id) {
        logger.info("GET request to get presupuesto with id: {}", id);
        try {
            PresupuestoDTO presupuestoDTO = presupuestoService.findById(id);
            logger.info("Returning presupuesto with id: {}", id);
            return ResponseEntity.ok(presupuestoDTO);
        } catch (Exception e) {
            logger.warn("Presupuesto with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PresupuestoDTO> createPresupuesto(@RequestBody PresupuestoDTO presupuestoDTO) {
        logger.info("POST request to create presupuesto");
        PresupuestoDTO createdPresupuesto = presupuestoService.save(presupuestoDTO);
        logger.info("Created presupuesto with id: {}", createdPresupuesto.getId());
        return ResponseEntity.ok(createdPresupuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoDTO> updatePresupuesto(@PathVariable Integer id, @RequestBody PresupuestoDTO presupuestoDTO) {
        logger.info("PUT request to update presupuesto with id: {}", id);
        try {
            PresupuestoDTO updatedPresupuesto = presupuestoService.update(id, presupuestoDTO);
            logger.info("Updated presupuesto with id: {}", updatedPresupuesto.getId());
            return ResponseEntity.ok(updatedPresupuesto);
        } catch (Exception e) {
            logger.warn("Presupuesto with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePresupuesto(@PathVariable Integer id) {
        logger.info("DELETE request to delete presupuesto with id: {}", id);
        try {
            presupuestoService.delete(id);
            logger.info("Deleted presupuesto with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warn("Presupuesto with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/client")
    public ResponseEntity<List<PresupuestoDTO>> getPresupuestoByClientId(@RequestBody UsuarioDTO id) {
        logger.info("POST request to get presupuestos by client id");
        ResponseEntity<List<PresupuestoDTO>> response = presupuestoService.findByClient(id);
        logger.info("Returning {} presupuestos for client", response.getBody().size());
        return response;
    }


    @PostMapping("/upload")
    public ResponseEntity<Void> uploadImagene(@RequestBody FileUpload imagenDTO) {
        logger.info("POST request to upload presupuesto file");
        try {
            presupuestoService.uploadFile(imagenDTO);
            logger.info("Uploaded presupuesto file successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error uploading presupuesto file", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
