package TFG.Terranaturale.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * DTO for returning user information without sensitive data.
 */
public class UsuarioResponseDTO {
    private Integer id;
    private String userName;
    private String nombre;
    private String apellidos;
    private String correo;
    private String dni;
    private String rol;
    private String telefono;
    private String direccion;

    // Explicitly ignore password field to ensure it's never serialized
    @JsonIgnore
    private String contraseña;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Getter for contraseña is intentionally omitted to prevent accidental exposure
    
    @JsonIgnore
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}