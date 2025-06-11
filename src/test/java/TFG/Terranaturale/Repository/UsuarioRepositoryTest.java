package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void whenFindByUserName_thenReturnUsuario() {
        // given
        Usuario usuario = new Usuario();
        usuario.setNombre("Test");
        usuario.setApellidos("User");
        usuario.setContraseña("password");
        usuario.setCorreo("test@example.com");
        usuario.setDni("12345678A");
        usuario.setRol("USER");
        usuario.setTelefono("123456789");
        usuario.setDireccion("Test Address");
        usuario.setUserName("testuser");
        
        entityManager.persist(usuario);
        entityManager.flush();
        
        // when
        Optional<Usuario> found = usuarioRepository.findByUserName(usuario.getUserName());
        
        // then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo(usuario.getNombre());
        assertThat(found.get().getApellidos()).isEqualTo(usuario.getApellidos());
        assertThat(found.get().getCorreo()).isEqualTo(usuario.getCorreo());
        assertThat(found.get().getDni()).isEqualTo(usuario.getDni());
        assertThat(found.get().getRol()).isEqualTo(usuario.getRol());
        assertThat(found.get().getTelefono()).isEqualTo(usuario.getTelefono());
        assertThat(found.get().getDireccion()).isEqualTo(usuario.getDireccion());
        assertThat(found.get().getUserName()).isEqualTo(usuario.getUserName());
    }
    
    @Test
    public void whenFindByUserNameWithNonExistingUsername_thenReturnEmpty() {
        // when
        Optional<Usuario> found = usuarioRepository.findByUserName("nonexistingusername");
        
        // then
        assertThat(found).isEmpty();
    }
    
    @Test
    public void whenFindById_thenReturnUsuario() {
        // given
        Usuario usuario = new Usuario();
        usuario.setNombre("Test");
        usuario.setApellidos("User");
        usuario.setContraseña("password");
        usuario.setCorreo("test@example.com");
        usuario.setDni("12345678A");
        usuario.setRol("USER");
        usuario.setTelefono("123456789");
        usuario.setDireccion("Test Address");
        usuario.setUserName("testuser");
        
        Usuario savedUsuario = entityManager.persist(usuario);
        entityManager.flush();
        
        // when
        Optional<Usuario> found = usuarioRepository.findById(savedUsuario.getId());
        
        // then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo(usuario.getNombre());
    }
}