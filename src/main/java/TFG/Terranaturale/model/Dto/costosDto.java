package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class costosDto {
    private Integer id;
    private String costPerUnit;
    private String name;
    private Integer idUnit;
    private Integer idCostCategory;
}
