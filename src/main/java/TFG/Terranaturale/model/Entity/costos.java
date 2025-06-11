package TFG.Terranaturale.model.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "costos")
public class costos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "costo_por_unidad", nullable = false)
    private String costPerUnit;

    @Column(name = "nombre", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_unidad", nullable = false)
    private unit idUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_de_coste", referencedColumnName = "id")
    private CostCategory idCostCategory;
}
