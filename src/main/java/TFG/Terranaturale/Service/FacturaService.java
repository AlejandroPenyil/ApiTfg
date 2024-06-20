package TFG.Terranaturale.Service;

import TFG.Terranaturale.Dto.FacturaDTO;
import TFG.Terranaturale.Dto.FileUpload;
import TFG.Terranaturale.Dto.ImageneDTO;
import TFG.Terranaturale.Dto.UsuarioDTO;
import TFG.Terranaturale.Exception.ResourceNotFoundException;
import TFG.Terranaturale.Model.Factura;
import TFG.Terranaturale.Model.Imagene;
import TFG.Terranaturale.Model.Usuario;
import TFG.Terranaturale.Repository.FacturaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    FacturaRepository facturaRepository;
    ModelMapper modelMapper;

    public FacturaService(FacturaRepository facturaRepository, ModelMapper mapper) {
        this.facturaRepository = facturaRepository;
        this.modelMapper = mapper;
    }

    public List<FacturaDTO> findAll() {
        return facturaRepository.findAll().stream()
                .map(factura -> modelMapper.map(factura, FacturaDTO.class))
                .collect(Collectors.toList());
    }

    public FacturaDTO findById(Integer id) {
        return facturaRepository.findById(id)
                .map(factura -> modelMapper.map(factura, FacturaDTO.class))
                .orElse(null);
    }

    public FacturaDTO save(FacturaDTO facturaDTO) {
        Factura factura = modelMapper.map(facturaDTO, Factura.class);
        factura = facturaRepository.save(factura);
        return modelMapper.map(factura, FacturaDTO.class);
    }

    public void deleteById(Integer id) {
        facturaRepository.deleteById(id);
    }

    public ResponseEntity<List<FacturaDTO>> findByClient(UsuarioDTO id) {
        Usuario usuario = modelMapper.map(id, Usuario.class);
        List<Factura> solicitudes = facturaRepository.findByIdCliente_Id(usuario.getId());



        List<FacturaDTO> facturaDTOList = new ArrayList<>();
        for (Factura factura : solicitudes) {
            facturaDTOList.add(modelMapper.map(factura, FacturaDTO.class));
        }
        return ResponseEntity.ok(facturaDTOList);
    }

    public ResponseEntity<InputStreamResource> downloadFactura(Integer id) throws IOException {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura not found"));
        File file = new File(factura.getUbicacion());
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void uploadFile(FileUpload imagenDTO) {
        String base64Content = imagenDTO.getContent();

        try {
            // Decodificar la cadena Base64 a bytes
            byte[] bytes = Base64.getDecoder().decode(base64Content);
            String random = RandomStringGenerator.generateRandomString(8);

            // Guardar los bytes en un archivo
            saveToFile(bytes, "C:\\Terranaturale\\Documents\\Facturas\\" + random + imagenDTO.getFileName());

            FacturaDTO immageneSaveDTO = new FacturaDTO();
            immageneSaveDTO.setIdCliente(9);
            immageneSaveDTO.setUbicacion("C:\\Terranaturale\\Documents\\Facturas\\" + random + imagenDTO.getFileName());

            Factura imagene = modelMapper.map(immageneSaveDTO, Factura.class);
            imagene.setFecha(Instant.now());

            facturaRepository.save(imagene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(byte[] content, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(content);
        }
    }
}
