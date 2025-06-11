package TFG.Terranaturale.Service;

import TFG.Terranaturale.model.Dto.PasswordResetRequest;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import TFG.Terranaturale.model.Exception.ResourceNotFoundException;
import TFG.Terranaturale.model.Exception.InvalidCredentialsException;

import TFG.Terranaturale.model.Entity.Usuario;
import TFG.Terranaturale.Repository.UsuarioRepository;
import TFG.Terranaturale.Util.PasswordUtils;
import TFG.Terranaturale.dto.UsuarioResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final PasswordUtils passwordUtils;

    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, PasswordUtils passwordUtils) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.passwordUtils = passwordUtils;
    }

    /**
     * Get all users without sensitive information.
     */
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> usuarioResponseDTOs = usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(usuarioResponseDTOs);
    }

    /**
     * Get user by ID without sensitive information.
     */
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(Integer id) {
        return ResponseEntity.ok().body(
                modelMapper.map(usuarioRepository.findById(id), UsuarioResponseDTO.class)
        );
    }

    /**
     * Create or update a user.
     * Returns user data without sensitive information.
     */
    public ResponseEntity<UsuarioResponseDTO> createOrUpdateUsuario(UsuarioDTO usuario) {
        try {
        // Validaciones básicas
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuarioEntity = modelMapper.map(usuario, Usuario.class);

        // Encrypt password before storing
        String rawPassword = usuario.getContraseña();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            String encryptedPassword = passwordUtils.encryptPassword(rawPassword);
            usuarioEntity.setContraseña(encryptedPassword);
        }

        // Guardar con manejo de excepciones
        Usuario savedUsuario = usuarioRepository.save(usuarioEntity);
        UsuarioResponseDTO usuarioResponseDTO = modelMapper.map(savedUsuario, UsuarioResponseDTO.class);
        return ResponseEntity.ok().body(usuarioResponseDTO);

    } catch (DataIntegrityViolationException e) {
        // Manejar violaciones de integridad de datos
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
        // Manejar otras excepciones
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Find a user by username.
     * Returns user data without sensitive information.
     */
    public UsuarioResponseDTO findByUserName(String userName) {
        Usuario usuario = usuarioRepository.findByUserName(userName)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        // Return user data without sensitive information
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    /**
     * Reset a user's password.
     * 
     * @param resetRequest the password reset request containing identifier and new password
     * @return ResponseEntity with the updated user data without sensitive information
     */
    public ResponseEntity<UsuarioResponseDTO> resetPassword(PasswordResetRequest resetRequest) {
        if (resetRequest == null || resetRequest.getIdentifier() == null || resetRequest.getNewPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Try to find user by username
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUserName(resetRequest.getIdentifier());

        // If not found by username, try by email
        if (!usuarioOptional.isPresent()) {
            usuarioOptional = usuarioRepository.findByCorreo(resetRequest.getIdentifier());
        }

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Encrypt the new password
            String encryptedPassword = passwordUtils.encryptPassword(resetRequest.getNewPassword());
            usuario.setContraseña(encryptedPassword);

            // Save the updated user
            usuario = usuarioRepository.save(usuario);

            // Return the updated user without sensitive information
            return ResponseEntity.ok(modelMapper.map(usuario, UsuarioResponseDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
