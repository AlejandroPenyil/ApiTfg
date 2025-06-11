package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.JardineService;
import TFG.Terranaturale.model.Dto.JardineDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jardines")
public class JardineController {
    private static final Logger logger = LoggerFactory.getLogger(JardineController.class);

    private final JardineService jardineService;

    public JardineController(JardineService jardineService) {
        this.jardineService = jardineService;
    }

    @GetMapping
    public ResponseEntity<List<JardineDTO>> getAllJardines() {
        logger.info("GET request to get all jardines");
        List<JardineDTO> jardines = jardineService.findAll();
        logger.info("Returning {} jardines", jardines.size());
        return ResponseEntity.ok(jardines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JardineDTO> getJardineById(@PathVariable Integer id) {
        logger.info("GET request to get jardine with id: {}", id);
        JardineDTO jardineDTO = jardineService.findById(id);
        if (jardineDTO == null) {
            logger.warn("Jardine with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Returning jardine with id: {}", id);
        return ResponseEntity.ok(jardineDTO);
    }

    @PostMapping
    public ResponseEntity<JardineDTO> createJardine(@RequestBody JardineDTO jardineDTO) {
        logger.info("POST request to create jardine");
        JardineDTO createdJardine = jardineService.save(jardineDTO);
        logger.info("Created jardine with id: {}", createdJardine.getId());
        return ResponseEntity.ok(createdJardine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JardineDTO> updateJardine(@PathVariable Integer id, @RequestBody JardineDTO jardineDTO) {
        logger.info("PUT request to update jardine with id: {}", id);
        JardineDTO existingJardine = jardineService.findById(id);
        if (existingJardine == null) {
            logger.warn("Jardine with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
        JardineDTO updatedJardine = jardineService.update(id, jardineDTO);
        logger.info("Updated jardine with id: {}", updatedJardine.getId());
        return ResponseEntity.ok(updatedJardine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJardine(@PathVariable Integer id) {
        logger.info("DELETE request to delete jardine with id: {}", id);
        JardineDTO existingJardine = jardineService.findById(id);
        if (existingJardine == null) {
            logger.warn("Jardine with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
        jardineService.delete(id);
        logger.info("Deleted jardine with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
