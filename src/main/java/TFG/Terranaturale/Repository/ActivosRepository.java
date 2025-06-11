package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.Activos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivosRepository extends JpaRepository<Activos, Integer>, JpaSpecificationExecutor<Activos> {
}
