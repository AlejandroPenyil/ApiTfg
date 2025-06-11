package TFG.Terranaturale.Service;

import TFG.Terranaturale.Repository.DocumentRepository;
import TFG.Terranaturale.Repository.FacturaRepository;
import TFG.Terranaturale.Repository.DetalleFacturaRepository;
import TFG.Terranaturale.Repository.CostosRepository;
import TFG.Terranaturale.model.Dto.DetalleFacturaDTO;
import TFG.Terranaturale.model.Dto.FacturaDTO;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import TFG.Terranaturale.model.Entity.DetalleFactura;
import TFG.Terranaturale.model.Entity.Documentos;
import TFG.Terranaturale.model.Entity.Factura;
import TFG.Terranaturale.model.Entity.Usuario;
import TFG.Terranaturale.model.Entity.costos;
import TFG.Terranaturale.model.Exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final CostosRepository costosRepository;
    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;

    public FacturaService(FacturaRepository facturaRepository, 
                         DetalleFacturaRepository detalleFacturaRepository,
                         CostosRepository costosRepository,
                         DocumentRepository documentRepository,
                         ModelMapper mapper) {
        this.facturaRepository = facturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.costosRepository = costosRepository;
        this.documentRepository = documentRepository;
        this.modelMapper = mapper;
    }

    public List<FacturaDTO> findAll() {
        return facturaRepository.findAll().stream()
                .map(factura -> convertToDTO(factura))
                .collect(Collectors.toList());
    }

    public FacturaDTO findById(Integer id) {
        return facturaRepository.findById(id)
                .map(factura -> convertToDTO(factura))
                .orElseThrow(() -> new ResourceNotFoundException("Factura not found with id " + id));
    }

    @Transactional
    public FacturaDTO save(FacturaDTO facturaDTO) {
        // Create a new document for the invoice
        Documentos documento = new Documentos();
        documento.setName("Factura " + facturaDTO.getNumeroFactura());
        documento.setIsFolder(false);
        documento.setFecha(ZonedDateTime.now());

        // Set the client for the document
        Usuario cliente = new Usuario();
        cliente.setId(facturaDTO.getIdCliente());
        documento.setIdCliente(cliente);

        // Save the document
        Documentos savedDocumento = documentRepository.save(documento);

        // Create the invoice
        Factura factura = new Factura();
        factura.setFecha(facturaDTO.getFecha() != null ? facturaDTO.getFecha() : ZonedDateTime.now());
        factura.setIdCliente(cliente);
        factura.setIdDocument(savedDocumento);
        factura.setNumeroFactura(facturaDTO.getNumeroFactura());
        factura.setTotal(facturaDTO.getTotal());
        factura.setSubtotal(facturaDTO.getSubtotal());
        factura.setIva(facturaDTO.getIva());
        factura.setDescuento(facturaDTO.getDescuento());
        factura.setNotas(facturaDTO.getNotas());

        // Save the invoice
        Factura savedFactura = facturaRepository.save(factura);

        // Process invoice details
        if (facturaDTO.getDetalles() != null && !facturaDTO.getDetalles().isEmpty()) {
            for (DetalleFacturaDTO detalleDTO : facturaDTO.getDetalles()) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setIdFactura(savedFactura);

                // Get the cost item
                costos costo = costosRepository.findById(detalleDTO.getIdCosto())
                    .orElseThrow(() -> new ResourceNotFoundException("Costo not found with id " + detalleDTO.getIdCosto()));
                detalle.setIdCosto(costo);

                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getSubtotal());
                detalle.setDescripcion(detalleDTO.getDescripcion());

                detalleFacturaRepository.save(detalle);
            }
        }

        return convertToDTO(savedFactura);
    }

    @Transactional
    public FacturaDTO update(Integer id, FacturaDTO facturaDTO) {
        // Check if the factura exists
        Factura existingFactura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura not found with id " + id));

        // Update invoice fields
        if (facturaDTO.getFecha() != null) {
            existingFactura.setFecha(facturaDTO.getFecha());
        }
        if (facturaDTO.getNumeroFactura() != null) {
            existingFactura.setNumeroFactura(facturaDTO.getNumeroFactura());
        }
        if (facturaDTO.getTotal() != null) {
            existingFactura.setTotal(facturaDTO.getTotal());
        }
        if (facturaDTO.getSubtotal() != null) {
            existingFactura.setSubtotal(facturaDTO.getSubtotal());
        }
        if (facturaDTO.getIva() != null) {
            existingFactura.setIva(facturaDTO.getIva());
        }
        if (facturaDTO.getDescuento() != null) {
            existingFactura.setDescuento(facturaDTO.getDescuento());
        }
        if (facturaDTO.getNotas() != null) {
            existingFactura.setNotas(facturaDTO.getNotas());
        }

        // Update client if provided
        if (facturaDTO.getIdCliente() != null) {
            Usuario cliente = new Usuario();
            cliente.setId(facturaDTO.getIdCliente());
            existingFactura.setIdCliente(cliente);
        }

        // Save the updated invoice
        Factura updatedFactura = facturaRepository.save(existingFactura);

        // Update invoice details if provided
        if (facturaDTO.getDetalles() != null && !facturaDTO.getDetalles().isEmpty()) {
            // Remove existing details
            detalleFacturaRepository.deleteByIdFactura(existingFactura);

            // Add new details
            for (DetalleFacturaDTO detalleDTO : facturaDTO.getDetalles()) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setIdFactura(updatedFactura);

                // Get the cost item
                costos costo = costosRepository.findById(detalleDTO.getIdCosto())
                    .orElseThrow(() -> new ResourceNotFoundException("Costo not found with id " + detalleDTO.getIdCosto()));
                detalle.setIdCosto(costo);

                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getSubtotal());
                detalle.setDescripcion(detalleDTO.getDescripcion());

                detalleFacturaRepository.save(detalle);
            }
        }

        return convertToDTO(updatedFactura);
    }

    @Transactional
    public void deleteById(Integer id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura not found with id " + id));

        // Delete invoice details
        detalleFacturaRepository.deleteByIdFactura(factura);

        // Delete the invoice
        facturaRepository.delete(factura);
    }

    public ResponseEntity<List<FacturaDTO>> findByClient(UsuarioDTO id) {
        Usuario usuario = modelMapper.map(id, Usuario.class);
        List<Factura> facturas = facturaRepository.findByIdCliente_Id(usuario.getId());

        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        for (Factura factura : facturas) {
            facturaDTOList.add(convertToDTO(factura));
        }
        return ResponseEntity.ok(facturaDTOList);
    }

    /**
     * Creates a new invoice with the given details
     * @param facturaDTO The invoice data
     * @param clientId The client ID
     * @return The created invoice
     */
    @Transactional
    public FacturaDTO createInvoice(FacturaDTO facturaDTO, Integer clientId) {
        // Set client ID
        facturaDTO.setIdCliente(clientId);

        // Set invoice number if not provided
        if (facturaDTO.getNumeroFactura() == null || facturaDTO.getNumeroFactura().isEmpty()) {
            String invoiceNumber = generateInvoiceNumber();
            facturaDTO.setNumeroFactura(invoiceNumber);
        }

        // Set date if not provided
        if (facturaDTO.getFecha() == null) {
            facturaDTO.setFecha(ZonedDateTime.now());
        }

        // Calculate totals if not provided
        if (facturaDTO.getDetalles() != null && !facturaDTO.getDetalles().isEmpty()) {
            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetalleFacturaDTO detalle : facturaDTO.getDetalles()) {
                // Calculate subtotal for each line item if not provided
                if (detalle.getSubtotal() == null) {
                    BigDecimal lineSubtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitario());
                    detalle.setSubtotal(lineSubtotal);
                }

                // Add to invoice subtotal
                subtotal = subtotal.add(detalle.getSubtotal());
            }

            // Set invoice subtotal if not provided
            if (facturaDTO.getSubtotal() == null) {
                facturaDTO.setSubtotal(subtotal);
            }

            // Calculate IVA (21%) if not provided
            if (facturaDTO.getIva() == null) {
                BigDecimal iva = facturaDTO.getSubtotal().multiply(new BigDecimal("0.21"));
                facturaDTO.setIva(iva);
            }

            // Set discount to zero if not provided
            if (facturaDTO.getDescuento() == null) {
                facturaDTO.setDescuento(BigDecimal.ZERO);
            }

            // Calculate total if not provided
            if (facturaDTO.getTotal() == null) {
                BigDecimal total = facturaDTO.getSubtotal().add(facturaDTO.getIva()).subtract(facturaDTO.getDescuento());
                facturaDTO.setTotal(total);
            }
        }

        return save(facturaDTO);
    }

    /**
     * Generates a unique invoice number
     * @return The generated invoice number
     */
    private String generateInvoiceNumber() {
        // Format: INV-YYYYMMDD-XXXX where XXXX is a sequential number
        String datePrefix = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Get the count of invoices for today
        long count = facturaRepository.countByNumeroFacturaStartingWith("INV-" + datePrefix);

        // Generate the invoice number
        return String.format("INV-%s-%04d", datePrefix, count + 1);
    }

    /**
     * Converts a Factura entity to a FacturaDTO
     * @param factura The entity to convert
     * @return The converted DTO
     */
    private FacturaDTO convertToDTO(Factura factura) {
        FacturaDTO dto = modelMapper.map(factura, FacturaDTO.class);

        // Load invoice details
        List<DetalleFactura> detalles = detalleFacturaRepository.findByIdFactura(factura);
        List<DetalleFacturaDTO> detallesDTO = new ArrayList<>();

        for (DetalleFactura detalle : detalles) {
            DetalleFacturaDTO detalleDTO = modelMapper.map(detalle, DetalleFacturaDTO.class);

            // Add additional information from the cost item
            detalleDTO.setNombreCosto(detalle.getIdCosto().getName());
            detalleDTO.setUnidadCosto(detalle.getIdCosto().getIdUnit().getCod());

            detallesDTO.add(detalleDTO);
        }

        dto.setDetalles(detallesDTO);
        return dto;
    }
}
