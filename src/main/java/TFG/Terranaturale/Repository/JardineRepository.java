package TFG.Terranaturale.Repository;


import TFG.Terranaturale.model.Entity.Jardine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JardineRepository extends JpaRepository<Jardine, Integer> {
}
