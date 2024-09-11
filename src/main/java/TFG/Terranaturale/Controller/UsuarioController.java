package TFG.Terranaturale.Controller;

import Dto.LoginRequest;
import Dto.UsuarioDTO;
import Exception.ResourceNotFoundException;
import Exception.InvalidCredentialsException;

import TFG.Terranaturale.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(id).getBody();
        return ResponseEntity.ok().body(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuario) {
        return usuarioService.createOrUpdateUsuario(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDetails) {
        UsuarioDTO usuario = usuarioService.getUsuarioById(id).getBody();
        if (usuario != null) {
            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setContraseña(usuarioDetails.getContraseña());
            usuario.setCorreo(usuarioDetails.getCorreo());
            usuario.setDni(usuarioDetails.getDni());
            usuario.setRol(usuarioDetails.getRol());
            usuario.setTelefono(usuarioDetails.getTelefono());
            usuario.setDireccion(usuarioDetails.getDireccion());
            usuario.setApellidos(usuarioDetails.getApellidos());
            usuarioService.createOrUpdateUsuario(usuario);
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.login(loginRequest.getUserName(), loginRequest.getContraseña());
            return ResponseEntity.ok(usuarioDTO);

        } catch (ResourceNotFoundException | InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
