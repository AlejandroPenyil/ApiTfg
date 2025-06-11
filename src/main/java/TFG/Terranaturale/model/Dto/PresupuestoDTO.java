package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PresupuestoDTO {
    private Integer id;
    private ZonedDateTime fechalEnvio;
    private ZonedDateTime fechaAceptado;
    private String estado;
    private Integer idJardin;
    private Integer idSolicitud;
    private String ubicacion;

    private String numeroPresupuesto;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal descuento;
    private String notas;
    private Integer validez;

    private List<DetallePresupuestoDTO> detalles = new ArrayList<>();
}
