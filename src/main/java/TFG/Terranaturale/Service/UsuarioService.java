package TFG.Terranaturale.Service;

import TFG.Terranaturale.Dto.UsuarioDTO;
import TFG.Terranaturale.Exception.InvalidCredentialsException;
import TFG.Terranaturale.Exception.ResourceNotFoundException;
import TFG.Terranaturale.Model.Usuario;
import TFG.Terranaturale.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
            usuarioDTOS.add(usuarioDTO);
        }


        return ResponseEntity.ok().body(usuarioDTOS);
    }

    public ResponseEntity<UsuarioDTO> getUsuarioById(Integer id) {
        return ResponseEntity.ok().body(modelMapper.map(usuarioRepository.findById(id), UsuarioDTO.class));
    }

    public ResponseEntity<UsuarioDTO> createOrUpdateUsuario(UsuarioDTO usuario) {
        Usuario usuarioEntity = modelMapper.map(usuario, Usuario.class);

        // Verificar si la contraseña proporcionada ya está encriptada
        if (!isPasswordEncrypted(usuario.getContraseña())) {
            // Si la contraseña no está encriptada, encriptarla antes de guardarla
            String encryptedPassword = passwordEncoder.encode(usuario.getContraseña());
            usuarioEntity.setContraseña(encryptedPassword);
        }

        // Guardar el usuario en la base de datos
        Usuario savedUsuario = usuarioRepository.save(usuarioEntity);

        // Mapear y devolver la respuesta
        UsuarioDTO usuarioDTO = modelMapper.map(savedUsuario, UsuarioDTO.class);
        return ResponseEntity.ok().body(usuarioDTO);
    }

    private boolean isPasswordEncrypted(String password) {
        // Puedes implementar tu propia lógica para verificar si la contraseña está encriptada
        // Por ejemplo, verificar si comienza con un prefijo específico o si sigue un formato determinado
        // En este ejemplo, asumimos que una contraseña encriptada comienza con "$2a$"
        return password.startsWith("$2a$");
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO login(String userName, String contraseña) {
        Usuario usuario = usuarioRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Nombre de usuario no encontrado"));

        if (!passwordEncoder.matches(contraseña, usuario.getContraseña())) {
            throw new InvalidCredentialsException("Contraseña incorrecta");
        }

        return modelMapper.map(usuario, UsuarioDTO.class);
    }
}