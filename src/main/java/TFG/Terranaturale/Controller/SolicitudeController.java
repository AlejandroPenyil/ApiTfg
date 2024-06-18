package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Dto.SolicitudeDTO;
import TFG.Terranaturale.Service.SolicitudeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudeController {

    private final SolicitudeService solicitudeService;

    public SolicitudeController(SolicitudeService solicitudeService) {
        this.solicitudeService = solicitudeService;
    }

    @GetMapping
    public List<SolicitudeDTO> getAllSolicitudes() {
        return solicitudeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudeDTO> getSolicitudeById(@PathVariable Integer id) {
        SolicitudeDTO solicitudeDTO = solicitudeService.findById(id);
        return ResponseEntity.ok(solicitudeDTO);
    }

    @PostMapping
    public ResponseEntity<SolicitudeDTO> createSolicitude(@RequestBody SolicitudeDTO solicitudeDTO) {
        SolicitudeDTO createdSolicitude = solicitudeService.save(solicitudeDTO);
        return ResponseEntity.ok(createdSolicitude);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudeDTO> updateSolicitude(@PathVariable Integer id, @RequestBody SolicitudeDTO solicitudeDTO) {
        SolicitudeDTO updatedSolicitude = solicitudeService.update(id, solicitudeDTO);
        return ResponseEntity.ok(updatedSolicitude);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitude(@PathVariable Integer id) {
        solicitudeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
