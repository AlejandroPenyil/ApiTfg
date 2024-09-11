package TFG.Terranaturale.Controller;

import Dto.ImageneDTO;
import Dto.ImageneUploadDto;
import Dto.UsuarioDTO;

import TFG.Terranaturale.Service.ImageneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imagenes")
public class ImageneController {

    private final ImageneService imageneService;

    @Autowired
    public ImageneController(ImageneService imageneService) {
        this.imageneService = imageneService;
    }

    @GetMapping
    public ResponseEntity<List<ImageneDTO>> getAllImagenes() {
        return imageneService.getAllImagenes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageneDTO> getImageneById(@PathVariable Integer id) {
        return imageneService.getImageneById(id);
    }

    @PostMapping("/cliente")
    public ResponseEntity<List<ImageneDTO>> getAllImageneByCliente(@RequestBody UsuarioDTO UsuarioDto) {
        return imageneService.getImageneByClientId(UsuarioDto);
    }

    @GetMapping("/invitado")
    public ResponseEntity<List<ImageneDTO>> getAllImageneByInvitado() {
        return imageneService.getImageneByInvitado();
    }

    @PostMapping
    public void insertImagene(@RequestBody ImageneDTO imagenDTO) {
        imageneService.createImagene(imagenDTO);
    }

    @PostMapping("/upload")
    public void uploadImagene(@RequestBody ImageneUploadDto imagenDTO) {
        imageneService.uploadImagene(imagenDTO);
    }

    @DeleteMapping
    public void deleteImagene(@RequestBody ImageneDTO imagenDTO) {
        imageneService.deleteImagene(imagenDTO);
    }
}

