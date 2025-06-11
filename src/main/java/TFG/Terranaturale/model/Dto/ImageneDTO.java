package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageneDTO {
    private Integer id;
    private String fecha;
    private String ubicacion;
    private Integer idJardin;
    private Integer idUsuario;
    private String comentario;
    private String imageDataBase64;
}
