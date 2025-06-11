package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MensajeDTO {
    private Integer id;
    private Integer idConversacion;
    private Integer idEmisor;
    private String nombreEmisor;
    private String rolEmisor;
    private String contenido;
    private ZonedDateTime fechaEnvio;
    private Boolean leido;
}