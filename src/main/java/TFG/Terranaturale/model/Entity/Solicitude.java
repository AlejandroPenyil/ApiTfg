package TFG.Terranaturale.model.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "solicitudes")
public class Solicitude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "fecha_solicitud", nullable = false)
    private ZonedDateTime fechaSolicitud;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @JoinColumn(name = "descripcion")
    private String descripcion;

    @JoinColumn(name = "atendida", nullable = false)
    private boolean atendida;

}