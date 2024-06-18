package TFG.Terranaturale.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class PresupuestoDTO {
    private Integer id;
    private Instant fechalEnvio;
    private Instant fechaAceptado;
    private String estado;
    private Integer idJardin;
    private Integer idSolicitud;
    private String ubicacion;
}
