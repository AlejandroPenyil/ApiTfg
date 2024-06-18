package TFG.Terranaturale.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class FacturaDTO {
    private Integer id;

    private Instant fecha;

    private Integer idCliente;

    private String ubicacion;
}
