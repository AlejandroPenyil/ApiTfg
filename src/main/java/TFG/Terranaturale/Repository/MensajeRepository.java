package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.Conversacion;
import TFG.Terranaturale.model.Entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findByIdConversacionOrderByFechaEnvioDesc(Conversacion conversacion);
    List<Mensaje> findByIdConversacionAndLeidoFalseOrderByFechaEnvioAsc(Conversacion conversacion);
}