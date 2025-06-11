package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.costos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostosRepository extends JpaRepository<costos, Integer> {
}