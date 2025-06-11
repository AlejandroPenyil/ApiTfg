package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository  extends JpaRepository<Documentos, Integer> {
}
