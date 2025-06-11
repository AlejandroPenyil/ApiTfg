package TFG.Terranaturale.Service;

import TFG.Terranaturale.Repository.UsuarioRepository;
import TFG.Terranaturale.Util.PasswordUtils;
import TFG.Terranaturale.dto.UsuarioResponseDTO;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import TFG.Terranaturale.model.Entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordUtils passwordUtils;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    public void setup() {
        // Setup test data
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Test");
        usuario.setApellidos("User");
        usuario.setContraseña("password");
        usuario.setCorreo("test@example.com");
        usuario.setDni("12345678A");
        usuario.setRol("USER");
        usuario.setTelefono("123456789");
        usuario.setDireccion("Test Address");
        usuario.setUserName("testuser");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1);
        usuarioDTO.setNombre("Test");
        usuarioDTO.setApellidos("User");
        usuarioDTO.setContraseña("password");
        usuarioDTO.setCorreo("test@example.com");
        usuarioDTO.setDni("12345678A");
        usuarioDTO.setRol("USER");
        usuarioDTO.setTelefono("123456789");
        usuarioDTO.setDireccion("Test Address");
        usuarioDTO.setUserName("testuser");

        usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(1);
        usuarioResponseDTO.setNombre("Test");
        usuarioResponseDTO.setApellidos("User");
        usuarioResponseDTO.setCorreo("test@example.com");
        usuarioResponseDTO.setDni("12345678A");
        usuarioResponseDTO.setRol("USER");
        usuarioResponseDTO.setTelefono("123456789");
        usuarioResponseDTO.setDireccion("Test Address");
        usuarioResponseDTO.setUserName("testuser");
    }

    @Test
    public void whenGetAllUsuarios_thenReturnAllUsuarios() {
        // given
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseDTO);

        // when
        ResponseEntity<List<UsuarioResponseDTO>> response = usuarioService.getAllUsuarios();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(usuario.getId());
        assertThat(response.getBody().get(0).getNombre()).isEqualTo(usuario.getNombre());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    public void whenGetUsuarioById_thenReturnUsuario() {
        // given
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(any(Optional.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseDTO);

        // when
        ResponseEntity<UsuarioResponseDTO> response = usuarioService.getUsuarioById(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(usuario.getId());
        assertThat(response.getBody().getNombre()).isEqualTo(usuario.getNombre());

        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    public void whenCreateUsuario_thenReturnCreatedUsuario() {
        // given
        when(modelMapper.map(any(UsuarioDTO.class), eq(Usuario.class))).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseDTO);
        // Mock password encryption
        when(passwordUtils.encryptPassword("password")).thenReturn("encrypted_password");

        // when
        ResponseEntity<UsuarioResponseDTO> response = usuarioService.createOrUpdateUsuario(usuarioDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(usuario.getId());
        assertThat(response.getBody().getNombre()).isEqualTo(usuario.getNombre());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void whenDeleteUsuario_thenRepositoryMethodIsCalled() {
        // when
        usuarioService.deleteUsuario(1);

        // then
        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenFindByUserName_thenReturnUsuario() {
        // given
        when(usuarioRepository.findByUserName("testuser")).thenReturn(Optional.of(usuario));
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseDTO);

        // when
        UsuarioResponseDTO response = usuarioService.findByUserName("testuser");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(usuario.getId());
        assertThat(response.getNombre()).isEqualTo(usuario.getNombre());

        verify(usuarioRepository, times(1)).findByUserName("testuser");
    }

    @Test
    public void whenFindByUserNameWithNonExistingUsername_thenReturnNull() {
        // given
        when(usuarioRepository.findByUserName("nonexistinguser")).thenReturn(Optional.empty());

        // when
        UsuarioResponseDTO response = usuarioService.findByUserName("nonexistinguser");

        // then
        assertThat(response).isNull();

        verify(usuarioRepository, times(1)).findByUserName("nonexistinguser");
    }
}
