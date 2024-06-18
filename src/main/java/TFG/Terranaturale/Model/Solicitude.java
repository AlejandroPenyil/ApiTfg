package TFG.Terranaturale.Model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

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
    private Instant fechaSolicitud;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @JoinColumn(name = "descripcion")
    private String descripcion;

    @JoinColumn(name = "atendida", nullable = false)
    private boolean atendida;

}