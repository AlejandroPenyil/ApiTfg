package TFG.Terranaturale.Filters;

import TFG.Terranaturale.model.Dto.ActivosDto;
import TFG.Terranaturale.model.Entity.Activos;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class ActivoSpecification {
    // Méthod que construye la Specification basada en el ActivosDto
    public static Specification<Activos> getActivosByFilters(ActivosDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por id si no es nulo
            if (filter.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), filter.getId()));
            }

            // Filtro por cantidad si no es nulo
            if (filter.getQuantity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("quantity"), filter.getQuantity()));
            }

            // Filtro por nombre si no es nulo
            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
            }

            // Combina los predicados usando AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
