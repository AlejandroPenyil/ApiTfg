package TFG.Terranaturale.Repository;

import TFG.Terranaturale.Model.Imagene;
import TFG.Terranaturale.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageneRepository extends JpaRepository<Imagene, Integer> {
    List<Imagene> findByIdUsuario(Usuario idUsuario);
}

