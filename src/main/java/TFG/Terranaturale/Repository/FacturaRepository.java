package TFG.Terranaturale.Repository;

import Entity.Factura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
        List<Factura> findByIdCliente_Id(Integer idCliente);
}
