package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Dto.PresupuestoDTO;
import TFG.Terranaturale.Dto.UsuarioDTO;
import TFG.Terranaturale.Service.PresupuestoService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/presupuestos")
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @GetMapping
    public List<PresupuestoDTO> getAllPresupuestos() {
        return presupuestoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoDTO> getPresupuestoById(@PathVariable Integer id) {
        PresupuestoDTO presupuestoDTO = presupuestoService.findById(id);
        return ResponseEntity.ok(presupuestoDTO);
    }

    @PostMapping
    public ResponseEntity<PresupuestoDTO> createPresupuesto(@RequestBody PresupuestoDTO presupuestoDTO) {
        PresupuestoDTO createdPresupuesto = presupuestoService.save(presupuestoDTO);
        return ResponseEntity.ok(createdPresupuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoDTO> updatePresupuesto(@PathVariable Integer id, @RequestBody PresupuestoDTO presupuestoDTO) {
        PresupuestoDTO updatedPresupuesto = presupuestoService.update(id, presupuestoDTO);
        return ResponseEntity.ok(updatedPresupuesto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePresupuesto(@PathVariable Integer id) {
        presupuestoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/client")
    public ResponseEntity<List<PresupuestoDTO>> getPresupuestoByClientId(@RequestBody UsuarioDTO id) {
        return presupuestoService.findByClient(id);
    }

    @GetMapping("/douwnload/{id}")
    public ResponseEntity<InputStreamResource> downloadPresupuesto(@PathVariable Integer id) throws IOException {
        return presupuestoService.download(id);
    }
}
