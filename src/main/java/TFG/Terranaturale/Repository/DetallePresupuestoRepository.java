package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.DetallePresupuesto;
import TFG.Terranaturale.model.Entity.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePresupuestoRepository extends JpaRepository<DetallePresupuesto, Integer> {
    List<DetallePresupuesto> findByIdPresupuesto(Presupuesto presupuesto);
    void deleteByIdPresupuesto(Presupuesto presupuesto);
}