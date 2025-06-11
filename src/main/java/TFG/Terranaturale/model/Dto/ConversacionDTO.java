package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConversacionDTO {
    private Integer id;
    private Integer idUsuario;
    private String nombreUsuario;
    private ZonedDateTime fechaCreacion;
    private String titulo;
    private List<MensajeDTO> mensajes = new ArrayList<>();
}