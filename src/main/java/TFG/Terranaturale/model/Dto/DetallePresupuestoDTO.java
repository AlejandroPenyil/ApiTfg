package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DetallePresupuestoDTO {
    private Integer id;
    private Integer idPresupuesto;
    private Integer idCosto;
    private String nombreCosto;
    private String unidadCosto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String descripcion;
}