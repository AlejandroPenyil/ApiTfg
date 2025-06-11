package TFG.Terranaturale.Service;

import TFG.Terranaturale.Repository.CostosRepository;
import TFG.Terranaturale.Repository.DetallePresupuestoRepository;
import TFG.Terranaturale.Repository.DocumentRepository;
import TFG.Terranaturale.Repository.JardineRepository;
import TFG.Terranaturale.Repository.PresupuestoRepository;
import TFG.Terranaturale.Repository.SolicitudeRepository;
import TFG.Terranaturale.Util.RandomStringGenerator;
import TFG.Terranaturale.model.Dto.DetallePresupuestoDTO;
import TFG.Terranaturale.model.Dto.FileUpload;
import TFG.Terranaturale.model.Dto.PresupuestoDTO;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import TFG.Terranaturale.model.Entity.DetallePresupuesto;
import TFG.Terranaturale.model.Entity.Documentos;
import TFG.Terranaturale.model.Entity.Presupuesto;
import TFG.Terranaturale.model.Entity.Solicitude;
import TFG.Terranaturale.model.Entity.Usuario;
import TFG.Terranaturale.model.Entity.costos;
import TFG.Terranaturale.model.Exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final SolicitudeRepository solicitudeRepository;
    private final JardineRepository jardineRepository;
    private final DetallePresupuestoRepository detallePresupuestoRepository;
    private final CostosRepository costosRepository;
    private final DocumentRepository documentRepository;

    private final ModelMapper modelMapper;

    public PresupuestoService(PresupuestoRepository presupuestoRepository, 
                             SolicitudeRepository solicitudeRepository, 
                             JardineRepository jardineRepository, 
                             DetallePresupuestoRepository detallePresupuestoRepository,
                             CostosRepository costosRepository,
                             DocumentRepository documentRepository,
                             ModelMapper modelMapper) {
        this.presupuestoRepository = presupuestoRepository;
        this.solicitudeRepository = solicitudeRepository;
        this.jardineRepository = jardineRepository;
        this.detallePresupuestoRepository = detallePresupuestoRepository;
        this.costosRepository = costosRepository;
        this.documentRepository = documentRepository;
        this.modelMapper = modelMapper;
    }

    public List<PresupuestoDTO> findAll() {
        return presupuestoRepository.findAll().stream()
                .map(presupuesto -> convertToDTO(presupuesto))
                .collect(Collectors.toList());
    }

    public PresupuestoDTO findById(Integer id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto not found"));
        return convertToDTO(presupuesto);
    }

    /**
     * Converts a Presupuesto entity to a PresupuestoDTO
     * @param presupuesto The entity to convert
     * @return The converted DTO
     */
    private PresupuestoDTO convertToDTO(Presupuesto presupuesto) {
        PresupuestoDTO dto = modelMapper.map(presupuesto, PresupuestoDTO.class);

        // Load budget details
        List<DetallePresupuesto> detalles = detallePresupuestoRepository.findByIdPresupuesto(presupuesto);
        List<DetallePresupuestoDTO> detallesDTO = new ArrayList<>();

        for (DetallePresupuesto detalle : detalles) {
            DetallePresupuestoDTO detalleDTO = modelMapper.map(detalle, DetallePresupuestoDTO.class);

            // Add additional information from the cost item
            detalleDTO.setNombreCosto(detalle.getIdCosto().getName());
            detalleDTO.setUnidadCosto(detalle.getIdCosto().getIdUnit().getCod());

            detallesDTO.add(detalleDTO);
        }

        dto.setDetalles(detallesDTO);
        return dto;
    }

    @Transactional
    public PresupuestoDTO save(PresupuestoDTO presupuestoDTO) {
        // Create a new document for the budget
        Documentos documento = new Documentos();
        documento.setName("Presupuesto " + presupuestoDTO.getNumeroPresupuesto());
        documento.setIsFolder(false);
        documento.setFecha(ZonedDateTime.now());

        // Set the client for the document based on the garden's client
        if (presupuestoDTO.getIdJardin() != null) {
            Usuario cliente = jardineRepository.findById(presupuestoDTO.getIdJardin())
                .orElseThrow(() -> new ResourceNotFoundException("Jardín not found with id " + presupuestoDTO.getIdJardin()))
                .getIdCliente();
            documento.setIdCliente(cliente);
        }

        // Save the document
        Documentos savedDocumento = documentRepository.save(documento);

        // Create the budget
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setFechalEnvio(presupuestoDTO.getFechalEnvio() != null ? presupuestoDTO.getFechalEnvio() : ZonedDateTime.now());
        presupuesto.setFechaAceptado(presupuestoDTO.getFechaAceptado());
        presupuesto.setEstado(presupuestoDTO.getEstado());
        presupuesto.setIdDocument(savedDocumento);
        presupuesto.setNumeroPresupuesto(presupuestoDTO.getNumeroPresupuesto());
        presupuesto.setTotal(presupuestoDTO.getTotal());
        presupuesto.setSubtotal(presupuestoDTO.getSubtotal());
        presupuesto.setIva(presupuestoDTO.getIva());
        presupuesto.setDescuento(presupuestoDTO.getDescuento());
        presupuesto.setNotas(presupuestoDTO.getNotas());
        presupuesto.setValidez(presupuestoDTO.getValidez());

        // Set the garden
        if (presupuestoDTO.getIdJardin() != null) {
            presupuesto.setIdJardin(jardineRepository.findById(presupuestoDTO.getIdJardin())
                .orElseThrow(() -> new ResourceNotFoundException("Jardín not found with id " + presupuestoDTO.getIdJardin())));
        }

        // Set the solicitude if provided
        if (presupuestoDTO.getIdSolicitud() != null) {
            presupuesto.setIdSolicitud(solicitudeRepository.findById(presupuestoDTO.getIdSolicitud())
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud not found with id " + presupuestoDTO.getIdSolicitud())));
        }

        // Save the budget
        Presupuesto savedPresupuesto = presupuestoRepository.save(presupuesto);

        // Process budget details
        if (presupuestoDTO.getDetalles() != null && !presupuestoDTO.getDetalles().isEmpty()) {
            for (DetallePresupuestoDTO detalleDTO : presupuestoDTO.getDetalles()) {
                DetallePresupuesto detalle = new DetallePresupuesto();
                detalle.setIdPresupuesto(savedPresupuesto);

                // Get the cost item
                costos costo = costosRepository.findById(detalleDTO.getIdCosto())
                    .orElseThrow(() -> new ResourceNotFoundException("Costo not found with id " + detalleDTO.getIdCosto()));
                detalle.setIdCosto(costo);

                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getSubtotal());
                detalle.setDescripcion(detalleDTO.getDescripcion());

                detallePresupuestoRepository.save(detalle);
            }
        }

        return convertToDTO(savedPresupuesto);
    }

    @Transactional
    public PresupuestoDTO update(Integer id, PresupuestoDTO presupuestoDTO) {
        // Check if the presupuesto exists
        Presupuesto existingPresupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto not found with id " + id));

        // Update budget fields
        if (presupuestoDTO.getFechalEnvio() != null) {
            existingPresupuesto.setFechalEnvio(presupuestoDTO.getFechalEnvio());
        }
        if (presupuestoDTO.getFechaAceptado() != null) {
            existingPresupuesto.setFechaAceptado(presupuestoDTO.getFechaAceptado());
        }
        if (presupuestoDTO.getEstado() != null) {
            existingPresupuesto.setEstado(presupuestoDTO.getEstado());
        }
        if (presupuestoDTO.getNumeroPresupuesto() != null) {
            existingPresupuesto.setNumeroPresupuesto(presupuestoDTO.getNumeroPresupuesto());
        }
        if (presupuestoDTO.getTotal() != null) {
            existingPresupuesto.setTotal(presupuestoDTO.getTotal());
        }
        if (presupuestoDTO.getSubtotal() != null) {
            existingPresupuesto.setSubtotal(presupuestoDTO.getSubtotal());
        }
        if (presupuestoDTO.getIva() != null) {
            existingPresupuesto.setIva(presupuestoDTO.getIva());
        }
        if (presupuestoDTO.getDescuento() != null) {
            existingPresupuesto.setDescuento(presupuestoDTO.getDescuento());
        }
        if (presupuestoDTO.getNotas() != null) {
            existingPresupuesto.setNotas(presupuestoDTO.getNotas());
        }
        if (presupuestoDTO.getValidez() != null) {
            existingPresupuesto.setValidez(presupuestoDTO.getValidez());
        }

        // Update garden if provided
        if (presupuestoDTO.getIdJardin() != null) {
            existingPresupuesto.setIdJardin(jardineRepository.findById(presupuestoDTO.getIdJardin())
                .orElseThrow(() -> new ResourceNotFoundException("Jardín not found with id " + presupuestoDTO.getIdJardin())));
        }

        // Update solicitude if provided
        if (presupuestoDTO.getIdSolicitud() != null) {
            existingPresupuesto.setIdSolicitud(solicitudeRepository.findById(presupuestoDTO.getIdSolicitud())
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud not found with id " + presupuestoDTO.getIdSolicitud())));
        }

        // Save the updated budget
        Presupuesto updatedPresupuesto = presupuestoRepository.save(existingPresupuesto);

        // Update budget details if provided
        if (presupuestoDTO.getDetalles() != null && !presupuestoDTO.getDetalles().isEmpty()) {
            // Remove existing details
            detallePresupuestoRepository.deleteByIdPresupuesto(existingPresupuesto);

            // Add new details
            for (DetallePresupuestoDTO detalleDTO : presupuestoDTO.getDetalles()) {
                DetallePresupuesto detalle = new DetallePresupuesto();
                detalle.setIdPresupuesto(updatedPresupuesto);

                // Get the cost item
                costos costo = costosRepository.findById(detalleDTO.getIdCosto())
                    .orElseThrow(() -> new ResourceNotFoundException("Costo not found with id " + detalleDTO.getIdCosto()));
                detalle.setIdCosto(costo);

                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getSubtotal());
                detalle.setDescripcion(detalleDTO.getDescripcion());

                detallePresupuestoRepository.save(detalle);
            }
        }

        return convertToDTO(updatedPresupuesto);
    }

    @Transactional
    public void delete(Integer id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto not found with id " + id));

        // Delete budget details
        detallePresupuestoRepository.deleteByIdPresupuesto(presupuesto);

        // Delete the budget
        presupuestoRepository.delete(presupuesto);
    }

    public ResponseEntity<List<PresupuestoDTO>> findByClient(UsuarioDTO id) {
        Usuario usuario = modelMapper.map(id, Usuario.class);
        List<Solicitude> solicitudes = solicitudeRepository.findByIdUsuario(usuario);

        List<Presupuesto> presupuestoList = new ArrayList<>();

        for(Solicitude solicitude : solicitudes) {
            List<Presupuesto> presupuestos = presupuestoRepository.findByIdSolicitud(solicitude);
            presupuestoList.addAll(presupuestos);
        }

        List<PresupuestoDTO> presupuestoDTOList = new ArrayList<>();
        for (Presupuesto presupuesto : presupuestoList) {
            presupuestoDTOList.add(convertToDTO(presupuesto));
        }
        return ResponseEntity.ok(presupuestoDTOList);
    }

    /**
     * Creates a new budget with the given details
     * @param presupuestoDTO The budget data
     * @param jardinId The garden ID
     * @return The created budget
     */
    @Transactional
    public PresupuestoDTO createBudget(PresupuestoDTO presupuestoDTO, Integer jardinId) {
        // Set garden ID
        presupuestoDTO.setIdJardin(jardinId);

        // Set budget number if not provided
        if (presupuestoDTO.getNumeroPresupuesto() == null || presupuestoDTO.getNumeroPresupuesto().isEmpty()) {
            String budgetNumber = generateBudgetNumber();
            presupuestoDTO.setNumeroPresupuesto(budgetNumber);
        }

        // Set date if not provided
        if (presupuestoDTO.getFechalEnvio() == null) {
            presupuestoDTO.setFechalEnvio(ZonedDateTime.now());
        }

        // Set status if not provided
        if (presupuestoDTO.getEstado() == null || presupuestoDTO.getEstado().isEmpty()) {
            presupuestoDTO.setEstado("PENDIENTE");
        }

        // Set validity period if not provided (default 30 days)
        if (presupuestoDTO.getValidez() == null) {
            presupuestoDTO.setValidez(30);
        }

        // Calculate totals if not provided
        if (presupuestoDTO.getDetalles() != null && !presupuestoDTO.getDetalles().isEmpty()) {
            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetallePresupuestoDTO detalle : presupuestoDTO.getDetalles()) {
                // Calculate subtotal for each line item if not provided
                if (detalle.getSubtotal() == null) {
                    BigDecimal lineSubtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitario());
                    detalle.setSubtotal(lineSubtotal);
                }

                // Add to budget subtotal
                subtotal = subtotal.add(detalle.getSubtotal());
            }

            // Set budget subtotal if not provided
            if (presupuestoDTO.getSubtotal() == null) {
                presupuestoDTO.setSubtotal(subtotal);
            }

            // Calculate IVA (21%) if not provided
            if (presupuestoDTO.getIva() == null) {
                BigDecimal iva = presupuestoDTO.getSubtotal().multiply(new BigDecimal("0.21"));
                presupuestoDTO.setIva(iva);
            }

            // Set discount to zero if not provided
            if (presupuestoDTO.getDescuento() == null) {
                presupuestoDTO.setDescuento(BigDecimal.ZERO);
            }

            // Calculate total if not provided
            if (presupuestoDTO.getTotal() == null) {
                BigDecimal total = presupuestoDTO.getSubtotal().add(presupuestoDTO.getIva()).subtract(presupuestoDTO.getDescuento());
                presupuestoDTO.setTotal(total);
            }
        }

        return save(presupuestoDTO);
    }

    /**
     * Generates a unique budget number
     * @return The generated budget number
     */
    private String generateBudgetNumber() {
        // Format: PRES-YYYYMMDD-XXXX where XXXX is a sequential number
        String datePrefix = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Get the count of budgets for today
        long count = presupuestoRepository.countByNumeroPresupuestoStartingWith("PRES-" + datePrefix);

        // Generate the budget number
        return String.format("PRES-%s-%04d", datePrefix, count + 1);
    }

    /**
     * Uploads a file and creates a budget from it
     * This method is being replaced by the new data model approach
     * @param fileUpload The file upload data
     */
    @Transactional
    public void uploadFile(FileUpload fileUpload) {
        // Create a new budget DTO
        PresupuestoDTO presupuestoDTO = new PresupuestoDTO();
        presupuestoDTO.setIdJardin(fileUpload.getId());
        presupuestoDTO.setEstado("PENDIENTE");
        presupuestoDTO.setFechalEnvio(ZonedDateTime.now());

        // Generate a budget number
        presupuestoDTO.setNumeroPresupuesto(generateBudgetNumber());

        // Set default values
        presupuestoDTO.setSubtotal(BigDecimal.ZERO);
        presupuestoDTO.setIva(BigDecimal.ZERO);
        presupuestoDTO.setTotal(BigDecimal.ZERO);
        presupuestoDTO.setValidez(30); // 30 days validity

        // Add a note about the uploaded file
        presupuestoDTO.setNotas("Presupuesto creado a partir de archivo: " + fileUpload.getFileName());

        // Save the budget
        save(presupuestoDTO);
    }
}
