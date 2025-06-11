package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.UsuarioService;
import TFG.Terranaturale.Util.PasswordUtils;
import TFG.Terranaturale.dto.UsuarioResponseDTO;
import TFG.Terranaturale.model.Dto.LoginRequest;
import TFG.Terranaturale.model.Dto.PasswordResetRequest;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import TFG.Terranaturale.model.Entity.Usuario;
import TFG.Terranaturale.model.Exception.InvalidCredentialsException;
import TFG.Terranaturale.Repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for user management endpoints.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordUtils passwordUtils;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository, PasswordUtils passwordUtils) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.passwordUtils = passwordUtils;
    }

    /**
     * Get all users.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    /**
     * Get user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Integer id) {
        return usuarioService.getUsuarioById(id);
    }

    /**
     * Create a new user.
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@RequestBody UsuarioDTO usuario) {
        return usuarioService.createOrUpdateUsuario(usuario);
    }

    /**
     * Update a user.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDetails) {
        UsuarioResponseDTO usuario = usuarioService.getUsuarioById(id).getBody();
        if (usuario != null) {
            // Create a new DTO with the updated values
            UsuarioDTO updatedUsuario = new UsuarioDTO();
            updatedUsuario.setId(id);
            updatedUsuario.setUserName(usuario.getUserName());
            updatedUsuario.setNombre(usuarioDetails.getNombre());
            updatedUsuario.setContraseña(usuarioDetails.getContraseña());
            updatedUsuario.setCorreo(usuarioDetails.getCorreo());
            updatedUsuario.setDni(usuarioDetails.getDni());
            updatedUsuario.setRol(usuarioDetails.getRol());
            updatedUsuario.setTelefono(usuarioDetails.getTelefono());
            updatedUsuario.setDireccion(usuarioDetails.getDireccion());
            updatedUsuario.setApellidos(usuarioDetails.getApellidos());

            return usuarioService.createOrUpdateUsuario(updatedUsuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a user.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Login a user.
     */
    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> usuarioLogin(@RequestBody LoginRequest loginRequest) {
        // Find user by username
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUserName(loginRequest.getUserName());

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String storedPassword = usuario.getContraseña();
            String rawPassword = loginRequest.getContraseña();

            boolean passwordMatches = false;

            // Check if the stored password is in the expected format (salt:hash)
            if (storedPassword.contains(":")) {
                // Use the password verification method for properly encrypted passwords
                passwordMatches = passwordUtils.verifyPassword(rawPassword, storedPassword);
            } else {
                // For legacy passwords that might not be properly encrypted
                // This is a fallback for passwords that might have been stored in plaintext
                passwordMatches = storedPassword.equals(rawPassword);

                // If the password matches, update it to the encrypted format for future logins
                if (passwordMatches) {
                    String encryptedPassword = passwordUtils.encryptPassword(rawPassword);
                    usuario.setContraseña(encryptedPassword);
                    usuarioRepository.save(usuario);
                }
            }

            if (passwordMatches) {
                // Convert to DTO
                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setId(usuario.getId());
                usuarioDTO.setNombre(usuario.getNombre());
                usuarioDTO.setApellidos(usuario.getApellidos());
                usuarioDTO.setContraseña(usuario.getContraseña());
                usuarioDTO.setCorreo(usuario.getCorreo());
                usuarioDTO.setDni(usuario.getDni());
                usuarioDTO.setRol(usuario.getRol());
                usuarioDTO.setTelefono(usuario.getTelefono());
                usuarioDTO.setDireccion(usuario.getDireccion());
                usuarioDTO.setUserName(usuario.getUserName());

                return ResponseEntity.ok(usuarioDTO);
            }
        }

        // If authentication fails
        throw new InvalidCredentialsException("Invalid username or password");
    }

    /**
     * Reset a user's password.
     * 
     * @param resetRequest the password reset request containing identifier and new password
     * @return ResponseEntity with the updated user data without sensitive information
     */
    @PostMapping("/reset-password")
    public ResponseEntity<UsuarioResponseDTO> resetPassword(@RequestBody PasswordResetRequest resetRequest) {
        return usuarioService.resetPassword(resetRequest);
    }
}
