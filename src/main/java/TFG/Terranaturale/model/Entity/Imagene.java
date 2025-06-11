package TFG.Terranaturale.model.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "imagenes")
public class Imagene {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;

    @OneToOne
    @JoinColumn(name = "id_document", nullable = false)
    private Documentos idDocument;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_jardin", nullable = false)
    private Jardine idJardin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @JoinColumn(name = "comentario")
    private String Comentario;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;
}
