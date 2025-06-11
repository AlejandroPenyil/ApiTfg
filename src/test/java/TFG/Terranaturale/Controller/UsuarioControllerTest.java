package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Repository.UsuarioRepository;
import TFG.Terranaturale.Service.UsuarioService;
import TFG.Terranaturale.Util.PasswordUtils;
import TFG.Terranaturale.dto.UsuarioResponseDTO;
import TFG.Terranaturale.model.Dto.LoginRequest;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import TFG.Terranaturale.model.Entity.Usuario;
import TFG.Terranaturale.model.Exception.InvalidCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordUtils passwordUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTO;
    private UsuarioResponseDTO usuarioResponseDTO;
    private Usuario usuario;

    @BeforeEach
    public void setup() {
        // Setup test data
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
    }

    @Test
    public void getAllUsuarios_ShouldReturnAllUsuarios() throws Exception {
        // given
        List<UsuarioResponseDTO> usuarios = Arrays.asList(usuarioResponseDTO);
        when(usuarioService.getAllUsuarios()).thenReturn(new ResponseEntity<>(usuarios, HttpStatus.OK));

        // when & then
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Test")))
                .andExpect(jsonPath("$[0].userName", is("testuser")));

        verify(usuarioService, times(1)).getAllUsuarios();
    }

    @Test
    public void getUsuarioById_ShouldReturnUsuario() throws Exception {
        // given
        when(usuarioService.getUsuarioById(1)).thenReturn(new ResponseEntity<>(usuarioResponseDTO, HttpStatus.OK));

        // when & then
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Test")))
                .andExpect(jsonPath("$.userName", is("testuser")));

        verify(usuarioService, times(1)).getUsuarioById(1);
    }

    @Test
    public void createUsuario_ShouldCreateAndReturnUsuario() throws Exception {
        // given
        when(usuarioService.createOrUpdateUsuario(any(UsuarioDTO.class)))
                .thenReturn(new ResponseEntity<>(usuarioResponseDTO, HttpStatus.OK));

        // when & then
        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Test")))
                .andExpect(jsonPath("$.userName", is("testuser")));

        verify(usuarioService, times(1)).createOrUpdateUsuario(any(UsuarioDTO.class));
    }

    @Test
    public void updateUsuario_ShouldUpdateAndReturnUsuario() throws Exception {
        // given
        when(usuarioService.getUsuarioById(1)).thenReturn(new ResponseEntity<>(usuarioResponseDTO, HttpStatus.OK));
        when(usuarioService.createOrUpdateUsuario(any(UsuarioDTO.class)))
                .thenReturn(new ResponseEntity<>(usuarioResponseDTO, HttpStatus.OK));

        // when & then
        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Test")))
                .andExpect(jsonPath("$.userName", is("testuser")));

        verify(usuarioService, times(1)).getUsuarioById(1);
        verify(usuarioService, times(1)).createOrUpdateUsuario(any(UsuarioDTO.class));
    }

    @Test
    public void deleteUsuario_ShouldDeleteUsuario() throws Exception {
        // given
        doNothing().when(usuarioService).deleteUsuario(1);

        // when & then
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deleteUsuario(1);
    }

    @Test
    public void login_WithValidCredentials_ShouldReturnUsuario() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("testuser");
        loginRequest.setContraseña("password");

        when(usuarioRepository.findByUserName("testuser")).thenReturn(Optional.of(usuario));
        // Mock password verification to return true for valid credentials
        when(passwordUtils.verifyPassword("password", "password")).thenReturn(true);

        // when & then
        mockMvc.perform(post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Test")))
                .andExpect(jsonPath("$.userName", is("testuser")));

        verify(usuarioRepository, times(1)).findByUserName("testuser");
    }

    @Test
    public void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("testuser");
        loginRequest.setContraseña("wrongpassword");

        when(usuarioRepository.findByUserName("testuser")).thenReturn(Optional.of(usuario));
        // Mock password verification to return false for invalid credentials
        when(passwordUtils.verifyPassword("wrongpassword", "password")).thenReturn(false);

        // when & then
        mockMvc.perform(post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof InvalidCredentialsException;
                });

        verify(usuarioRepository, times(1)).findByUserName("testuser");
    }
}
