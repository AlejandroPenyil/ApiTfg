package TFG.Terranaturale.Repository;

import TFG.Terranaturale.model.Entity.DetalleFactura;
import TFG.Terranaturale.model.Entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Integer> {
    List<DetalleFactura> findByIdFactura(Factura factura);
    void deleteByIdFactura(Factura factura);
}