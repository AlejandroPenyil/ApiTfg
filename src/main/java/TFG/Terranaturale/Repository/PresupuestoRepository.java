package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.Presupuesto;
import TFG.Terranaturale.model.Entity.Solicitude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Integer> {
    List<Presupuesto> findByIdSolicitud(Solicitude solicitudeDTOS);
    long countByNumeroPresupuestoStartingWith(String prefix);
}
