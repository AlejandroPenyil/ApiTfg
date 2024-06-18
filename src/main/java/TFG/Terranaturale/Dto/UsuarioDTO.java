package TFG.Terranaturale.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String contraseña;
    private String correo;
    private String dni;
    private String rol;
    private String telefono;
    private String direccion;
    private String userName;
    private String apellidos;
}
