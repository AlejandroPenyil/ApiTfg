package TFG.Terranaturale.model.Filters;

import TFG.Terranaturale.model.Dto.ActivosDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivosFilterDto {
    private Integer page;        // Página a consultar
    private Integer size;        // Cantidad de elementos por página
    private ActivosDto filterContent;  // Lista de filtros basados en ActivosDto
}
