package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.SolicitudeService;
import TFG.Terranaturale.model.Dto.SolicitudeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudeController {
    private static final Logger logger = LoggerFactory.getLogger(SolicitudeController.class);

    private final SolicitudeService solicitudeService;

    public SolicitudeController(SolicitudeService solicitudeService) {
        this.solicitudeService = solicitudeService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudeDTO>> getAllSolicitudes() {
        logger.info("GET request to get all solicitudes");
        List<SolicitudeDTO> solicitudes = solicitudeService.findAll();
        logger.info("Returning {} solicitudes", solicitudes.size());
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudeDTO> getSolicitudeById(@PathVariable Integer id) {
        logger.info("GET request to get solicitude with id: {}", id);
        try {
            SolicitudeDTO solicitudeDTO = solicitudeService.findById(id);
            logger.info("Returning solicitude with id: {}", id);
            return ResponseEntity.ok(solicitudeDTO);
        } catch (Exception e) {
            logger.warn("Solicitude with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SolicitudeDTO> createSolicitude(@RequestBody SolicitudeDTO solicitudeDTO) {
        logger.info("POST request to create solicitude");
        SolicitudeDTO createdSolicitude = solicitudeService.save(solicitudeDTO);
        logger.info("Created solicitude with id: {}", createdSolicitude.getId());
        return ResponseEntity.ok(createdSolicitude);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudeDTO> updateSolicitude(@PathVariable Integer id, @RequestBody SolicitudeDTO solicitudeDTO) {
        logger.info("PUT request to update solicitude with id: {}", id);
        try {
            SolicitudeDTO updatedSolicitude = solicitudeService.update(id, solicitudeDTO);
            logger.info("Updated solicitude with id: {}", updatedSolicitude.getId());
            return ResponseEntity.ok(updatedSolicitude);
        } catch (Exception e) {
            logger.warn("Solicitude with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitude(@PathVariable Integer id) {
        logger.info("DELETE request to delete solicitude with id: {}", id);
        try {
            solicitudeService.delete(id);
            logger.info("Deleted solicitude with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warn("Solicitude with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}
