package TFG.Terranaturale.model.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "documentos")
public class Documentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "es_carpeta", nullable = false)
    private Boolean isFolder;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent", referencedColumnName = "id")
    private Documentos idParent;

}
