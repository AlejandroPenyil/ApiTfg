package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUserName(String userName);
    Optional<Usuario> findByCorreo(String correo);
}
