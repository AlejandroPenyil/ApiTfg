package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FacturaDTO {
    private Integer id;

    private ZonedDateTime fecha;

    private Integer idCliente;

    private String ubicacion;

    private String numeroFactura;

    private BigDecimal total;

    private BigDecimal subtotal;

    private BigDecimal iva;

    private BigDecimal descuento;

    private String notas;

    private List<DetalleFacturaDTO> detalles = new ArrayList<>();
}
