package TFG.Terranaturale.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudeDTO {
    private Integer id;
    private Instant fechaSolicitud;
    private Integer idUsuario;
    private String descripcion;
    private boolean atendida;
}
