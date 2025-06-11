package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.Conversacion;
import TFG.Terranaturale.model.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Integer> {
    Optional<Conversacion> findByIdUsuario(Usuario usuario);
}