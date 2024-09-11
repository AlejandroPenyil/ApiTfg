package TFG.Terranaturale.Repository;

import Entity.Solicitude;

import Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudeRepository extends JpaRepository<Solicitude, Integer> {
     List<Solicitude> findByIdUsuario(Usuario id);
}
