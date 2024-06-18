package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Dto.JardineDTO;
import TFG.Terranaturale.Service.JardineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jardines")
public class JardineController {

    private final JardineService jardineService;

    public JardineController(JardineService jardineService) {
        this.jardineService = jardineService;
    }

    @GetMapping
    public List<JardineDTO> getAllJardines() {
        return jardineService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JardineDTO> getJardineById(@PathVariable Integer id) {
        JardineDTO jardineDTO = jardineService.findById(id);
        return ResponseEntity.ok(jardineDTO);
    }

    @PostMapping
    public ResponseEntity<JardineDTO> createJardine(@RequestBody JardineDTO jardineDTO) {
        JardineDTO createdJardine = jardineService.save(jardineDTO);
        return ResponseEntity.ok(createdJardine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JardineDTO> updateJardine(@PathVariable Integer id, @RequestBody JardineDTO jardineDTO) {
        JardineDTO updatedJardine = jardineService.update(id, jardineDTO);
        return ResponseEntity.ok(updatedJardine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJardine(@PathVariable Integer id) {
        jardineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
