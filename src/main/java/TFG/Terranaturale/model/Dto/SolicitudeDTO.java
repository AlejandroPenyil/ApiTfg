package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudeDTO {
    private Integer id;
    private ZonedDateTime fechaSolicitud;
    private Integer idUsuario;
    private String descripcion;
    private boolean atendida;
}
