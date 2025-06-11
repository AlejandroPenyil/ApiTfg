package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.ActivosService;
import TFG.Terranaturale.model.Dto.ActivosDto;
import TFG.Terranaturale.model.Filters.ActivosFilterDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activos")
public class ActivosController {
    private static final Logger logger = LoggerFactory.getLogger(ActivosController.class);
    private final ActivosService activosService;

    public ActivosController(ActivosService activosService) {
        this.activosService = activosService;
    }

    @GetMapping
    public ResponseEntity<List<ActivosDto>> getAllActivos() {
        logger.info("GET request to get all activos");
        List<ActivosDto> activos = activosService.findAll();
        logger.info("Returning {} activos", activos.size());
        return ResponseEntity.ok(activos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivosDto> getActivoById(@PathVariable Integer id) {
        logger.info("GET request to get activo with id: {}", id);
        ActivosDto activosDto = activosService.findById(id);
        if (activosDto == null) {
            logger.warn("Activo with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Returning activo with id: {}", id);
        return ResponseEntity.ok(activosDto);
    }

    @PostMapping
    public ResponseEntity<ActivosDto> createActivo(@RequestBody ActivosDto activosDto) {
        logger.info("POST request to create activo: {}", activosDto.getName());
        ActivosDto createdActivo = activosService.save(activosDto);
        logger.info("Created activo with id: {}", createdActivo.getId());
        return ResponseEntity.ok(createdActivo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivosDto> updateActivo(@PathVariable Integer id, @RequestBody ActivosDto activosDto) {
        logger.info("PUT request to update activo with id: {}", id);
        ActivosDto existingActivo = activosService.findById(id);
        if (existingActivo == null) {
            logger.warn("Activo with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
        activosDto.setId(id);
        ActivosDto updatedActivo = activosService.save(activosDto);
        logger.info("Updated activo with id: {}", updatedActivo.getId());
        return ResponseEntity.ok(updatedActivo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivo(@PathVariable Integer id) {
        logger.info("DELETE request to delete activo with id: {}", id);
        ActivosDto existingActivo = activosService.findById(id);
        if (existingActivo == null) {
            logger.warn("Activo with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
        activosService.deleteById(id);
        logger.info("Deleted activo with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ActivosDto>> getActivosPaginadosConFiltro(
            @RequestBody ActivosFilterDto filter) {
        logger.info("POST request to filter activos - page: {}, size: {}", filter.getPage(), filter.getSize());

        if (filter.getFilterContent() != null) {
            logger.info("Filter criteria - name: {}, quantity: {}", 
                filter.getFilterContent().getName(), 
                filter.getFilterContent().getQuantity());
        }

        // Obtener la lista paginada desde el servicio basado en el filtro
        Page<ActivosDto> activos = activosService.getPaginatedActivosWithFilter(filter);

        logger.info("Returning {} filtered activos", activos.getContent().size());
        return ResponseEntity.ok(activos);
    }
}
