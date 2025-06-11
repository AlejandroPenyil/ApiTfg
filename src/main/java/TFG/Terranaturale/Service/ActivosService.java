package TFG.Terranaturale.Service;

import TFG.Terranaturale.model.Dto.ActivosDto;
import TFG.Terranaturale.model.Entity.Activos;
import TFG.Terranaturale.model.Filters.ActivosFilterDto;
import TFG.Terranaturale.Filters.ActivoSpecification;
import TFG.Terranaturale.Repository.ActivosRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivosService {
    private static final Logger logger = LoggerFactory.getLogger(ActivosService.class);
    private final ActivosRepository activosRepository;
    private final ModelMapper modelMapper;

    public ActivosService(ActivosRepository activosRepository, ModelMapper modelMapper) {
        this.activosRepository = activosRepository;
        this.modelMapper = modelMapper;
    }


    public List<ActivosDto> findAll() {
        logger.info("Finding all activos");
        List<ActivosDto> activosList = activosRepository.findAll().stream()
                .map(activo -> modelMapper.map(activo, ActivosDto.class))
                .collect(Collectors.toList());
        logger.info("Found {} activos", activosList.size());
        return activosList;
    }

    public ActivosDto findById(Integer id) {
        logger.info("Finding activo with id: {}", id);
        return activosRepository.findById(id)
                .map(activo -> {
                    logger.info("Found activo: {}", activo.getName());
                    return modelMapper.map(activo, ActivosDto.class);
                })
                .orElse(null);
    }

    public ActivosDto save(ActivosDto activosDto) {
        logger.info("Saving activo: {}", activosDto.getName());
        Activos activo = modelMapper.map(activosDto, Activos.class);
        activo = activosRepository.save(activo);
        logger.info("Saved activo with id: {}", activo.getId());
        return modelMapper.map(activo, ActivosDto.class);
    }

    public void deleteById(Integer id) {
        logger.info("Deleting activo with id: {}", id);
        activosRepository.deleteById(id);
        logger.info("Deleted activo with id: {}", id);
    }

    // Método para obtener una lista paginada de ActivosDto
    public Page<ActivosDto> getPaginatedActivos(int page, int size) {
        logger.info("Getting paginated activos - page: {}, size: {}", page, size);
        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Obtener la página de Activos desde el repositorio
        Page<Activos> activosPage = activosRepository.findAll(pageable);

        // Mapear la entidad Activo a ActivosDto usando ModelMapper
        Page<ActivosDto> result = activosPage.map(activo -> modelMapper.map(activo, ActivosDto.class));

        logger.info("Found {} activos in page {}", result.getContent().size(), page);
        return result;
    }

    public Page<ActivosDto> getPaginatedActivosWithFilter(ActivosFilterDto filter) {
        logger.info("Getting paginated activos with filter - page: {}, size: {}", filter.getPage(), filter.getSize());
        if (filter.getFilterContent() != null) {
            logger.info("Filter content: name={}, quantity={}", 
                filter.getFilterContent().getName(), 
                filter.getFilterContent().getQuantity());
        }

        // Crear el objeto Pageable usando los valores de page y size
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

        // Obtener los activos filtrados usando Specification, pasando el contenido del filtro
        Page<Activos> activosPage = activosRepository.findAll(
                ActivoSpecification.getActivosByFilters(filter.getFilterContent()),
                pageable
        );

        // Mapear la entidad Activo a ActivosDto usando ModelMapper
        Page<ActivosDto> result = activosPage.map(activo -> modelMapper.map(activo, ActivosDto.class));

        logger.info("Found {} filtered activos in page {}", result.getContent().size(), filter.getPage());
        return result;
    }
}
