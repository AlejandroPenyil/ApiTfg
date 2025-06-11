package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.ImageneService;
import TFG.Terranaturale.model.Dto.ImageneDTO;
import TFG.Terranaturale.model.Dto.ImageneUploadDto;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imagenes")
public class ImageneController {
    private static final Logger logger = LoggerFactory.getLogger(ImageneController.class);

    private final ImageneService imageneService;

    @Autowired
    public ImageneController(ImageneService imageneService) {
        this.imageneService = imageneService;
    }

    @GetMapping
    public ResponseEntity<List<ImageneDTO>> getAllImagenes() {
        logger.info("GET request to get all imagenes");
        ResponseEntity<List<ImageneDTO>> response = imageneService.getAllImagenes();
        logger.info("Returning {} imagenes", response.getBody().size());
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageneDTO> getImageneById(@PathVariable Integer id) {
        logger.info("GET request to get imagene with id: {}", id);
        ResponseEntity<ImageneDTO> response = imageneService.getImageneById(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Returning imagene with id: {}", id);
        } else {
            logger.warn("Imagene with id {} not found", id);
        }
        return response;
    }

    @PostMapping("/cliente")
    public ResponseEntity<List<ImageneDTO>> getAllImageneByCliente(@RequestBody UsuarioDTO UsuarioDto) {
        logger.info("POST request to get imagenes by cliente");
        ResponseEntity<List<ImageneDTO>> response = imageneService.getImageneByClientId(UsuarioDto);
        logger.info("Returning {} imagenes for cliente", response.getBody().size());
        return response;
    }

    @GetMapping("/invitado")
    public ResponseEntity<List<ImageneDTO>> getAllImageneByInvitado() {
        logger.info("GET request to get imagenes for invitado");
        ResponseEntity<List<ImageneDTO>> response = imageneService.getImageneByInvitado();
        logger.info("Returning {} imagenes for invitado", response.getBody().size());
        return response;
    }

    @PostMapping
    public ResponseEntity<ImageneDTO> insertImagene(@RequestBody ImageneDTO imagenDTO) {
        logger.info("POST request to create imagene");
        ResponseEntity<ImageneDTO> response = imageneService.createImagene(imagenDTO);
        logger.info("Created imagene with id: {}", response.getBody().getId());
        return response;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadImagene(@RequestBody ImageneUploadDto imagenDTO) {
        logger.info("POST request to upload imagene");
        try {
            imageneService.uploadImagene(imagenDTO);
            logger.info("Uploaded imagene successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error uploading imagene: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageneDTO> updateImagene(@PathVariable Integer id, @RequestBody ImageneDTO imagenDTO) {
        logger.info("PUT request to update imagene with id: {}", id);
        try {
            imagenDTO.setId(id);
            ResponseEntity<ImageneDTO> response = imageneService.updateImagene(id, imagenDTO);
            logger.info("Updated imagene with id: {}", response.getBody().getId());
            return response;
        } catch (Exception e) {
            logger.warn("Imagene with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImagene(@PathVariable Integer id) {
        logger.info("DELETE request to delete imagene with id: {}", id);
        try {
            imageneService.deleteImagene(id);
            logger.info("Deleted imagene with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting imagene with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}
