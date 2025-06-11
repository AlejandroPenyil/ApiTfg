package TFG.Terranaturale.model.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "presupuestos")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fechal_envio", nullable = false)
    private ZonedDateTime fechalEnvio;

    @Column(name = "fecha_aceptado")
    private ZonedDateTime fechaAceptado;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_jardin", nullable = false)
    private Jardine idJardin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud")
    private Solicitude idSolicitud;

    @OneToOne
    @JoinColumn(name = "id_document", nullable = false)
    private Documentos idDocument;

    @Column(name = "numero_presupuesto", nullable = false)
    private String numeroPresupuesto;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "iva", nullable = false, precision = 10, scale = 2)
    private BigDecimal iva;

    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(name = "notas", length = 500)
    private String notas;

    @Column(name = "validez")
    private Integer validez;

    @OneToMany(mappedBy = "idPresupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePresupuesto> detalles = new ArrayList<>();
}
