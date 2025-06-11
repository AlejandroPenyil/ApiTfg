package TFG.Terranaturale.Service;


import TFG.Terranaturale.Repository.DocumentRepository;
import TFG.Terranaturale.Repository.ImageneRepository;
import TFG.Terranaturale.Repository.JardineRepository;
import TFG.Terranaturale.Repository.UsuarioRepository;
import TFG.Terranaturale.model.Dto.ImageneDTO;
import TFG.Terranaturale.model.Dto.ImageneUploadDto;
import TFG.Terranaturale.model.Dto.UsuarioDTO;
import TFG.Terranaturale.model.Entity.Documentos;
import TFG.Terranaturale.model.Entity.Imagene;
import TFG.Terranaturale.model.Entity.Jardine;
import TFG.Terranaturale.model.Entity.Usuario;
import TFG.Terranaturale.model.Exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageneService {

    private final ImageneRepository imageneRepository;
    private final ModelMapper modelMapper;
    private final JardineRepository jardineRepository;
    private final UsuarioRepository usuarioRepository;
    private final DocumentRepository documentRepository;

    public ImageneService(ImageneRepository imageneRepository, ModelMapper modelMapper,
                         JardineRepository jardineRepository, UsuarioRepository usuarioRepository,
                         DocumentRepository documentRepository) {
        this.imageneRepository = imageneRepository;
        this.modelMapper = modelMapper;
        this.jardineRepository = jardineRepository;
        this.usuarioRepository = usuarioRepository;
        this.documentRepository = documentRepository;
    }

    public ResponseEntity<List<ImageneDTO>> getAllImagenes() {
        List<Imagene> imagenes = imageneRepository.findAll();
        List<ImageneDTO> imageneDTOS = new ArrayList<>();
        for (Imagene imagene : imagenes) {
            ImageneDTO imageneDTO = modelMapper.map(imagene, ImageneDTO.class);

            // Convert image data to Base64 if available
            if (imagene.getImageData() != null) {
                imageneDTO.setImageDataBase64(Base64.getEncoder().encodeToString(imagene.getImageData()));
            }

            imageneDTOS.add(imageneDTO);
        }
        return ResponseEntity.ok().body(imageneDTOS);
    }

    public ResponseEntity<ImageneDTO> getImageneById(Integer id) {
        Imagene imagene = imageneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Imagene not found with id " + id));

        ImageneDTO imageneDTO = modelMapper.map(imagene, ImageneDTO.class);

        // Convert image data to Base64 if available
        if (imagene.getImageData() != null) {
            imageneDTO.setImageDataBase64(Base64.getEncoder().encodeToString(imagene.getImageData()));
        }

        return ResponseEntity.ok().body(imageneDTO);
    }

    public ResponseEntity<List<ImageneDTO>> getImageneByClientId(UsuarioDTO userDto) {
        Usuario user = modelMapper.map(userDto, Usuario.class);
        List<Imagene> imageneList = imageneRepository.findByIdUsuario(user);

        List<ImageneDTO> imageneDTOS = new ArrayList<>();
        for (Imagene imagene : imageneList) {
            ImageneDTO imageneDTO = modelMapper.map(imagene, ImageneDTO.class);

            // Convert image data to Base64 if available
            if (imagene.getImageData() != null) {
                imageneDTO.setImageDataBase64(Base64.getEncoder().encodeToString(imagene.getImageData()));
            }

            imageneDTOS.add(imageneDTO);
        }
        return ResponseEntity.ok().body(imageneDTOS);
    }

    public ResponseEntity<ImageneDTO> createImagene(ImageneDTO imagenDTO) {
        // Create a new Imagene entity
        Imagene imagene = new Imagene();
        imagene.setFecha(ZonedDateTime.now());
        imagene.setComentario(imagenDTO.getComentario());

        // Set image data if available
        if (imagenDTO.getImageDataBase64() != null && !imagenDTO.getImageDataBase64().isEmpty()) {
            imagene.setImageData(Base64.getDecoder().decode(imagenDTO.getImageDataBase64()));
        }

        // Set the Jardine reference
        if (imagenDTO.getIdJardin() != null) {
            Jardine jardine = jardineRepository.findById(imagenDTO.getIdJardin())
                .orElseThrow(() -> new ResourceNotFoundException("Jardine not found with id " + imagenDTO.getIdJardin()));
            imagene.setIdJardin(jardine);
        }

        // Set the Usuario reference
        if (imagenDTO.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(imagenDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + imagenDTO.getIdUsuario()));
            imagene.setIdUsuario(usuario);
        }

        // Create and set the Document reference
        Documentos documento = new Documentos();
        documento.setName(imagenDTO.getComentario() != null ? imagenDTO.getComentario() : "Image");
        documento.setIsFolder(false);
        documento.setFecha(ZonedDateTime.now());

        // Set the user from the image
        if (imagenDTO.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(imagenDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + imagenDTO.getIdUsuario()));
            documento.setIdCliente(usuario);
        }

        // Save the document and set it in the image
        Documentos savedDocumento = documentRepository.save(documento);
        imagene.setIdDocument(savedDocumento);

        // Save the image
        Imagene savedImagene = imageneRepository.save(imagene);

        // Convert the saved entity back to DTO
        ImageneDTO savedImageneDTO = modelMapper.map(savedImagene, ImageneDTO.class);

        // Add the Base64 image data to the response if available
        if (savedImagene.getImageData() != null) {
            savedImageneDTO.setImageDataBase64(Base64.getEncoder().encodeToString(savedImagene.getImageData()));
        }

        return ResponseEntity.ok().body(savedImageneDTO);
    }

    public ResponseEntity<ImageneDTO> updateImagene(Integer id, ImageneDTO imagenDTO) {
        Imagene existingImagene = imageneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Imagene not found with id " + id));

        // Update fields from DTO
        if (imagenDTO.getComentario() != null) {
            existingImagene.setComentario(imagenDTO.getComentario());
        }

        // Update image data if provided
        if (imagenDTO.getImageDataBase64() != null && !imagenDTO.getImageDataBase64().isEmpty()) {
            existingImagene.setImageData(Base64.getDecoder().decode(imagenDTO.getImageDataBase64()));
        }

        // Note: ubicacion is stored in the DTO but not in the entity
        // It's handled by the ModelMapper when converting entity to DTO

        // Update Jardine reference
        if (imagenDTO.getIdJardin() != null) {
            Jardine jardine = jardineRepository.findById(imagenDTO.getIdJardin())
                .orElseThrow(() -> new ResourceNotFoundException("Jardine not found with id " + imagenDTO.getIdJardin()));
            existingImagene.setIdJardin(jardine);
        }

        // Update Usuario reference
        if (imagenDTO.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(imagenDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + imagenDTO.getIdUsuario()));
            existingImagene.setIdUsuario(usuario);
        }

        Imagene updatedImagene = imageneRepository.save(existingImagene);

        // Convert the updated entity back to DTO
        ImageneDTO updatedImageneDTO = modelMapper.map(updatedImagene, ImageneDTO.class);

        // Add the Base64 image data to the response if available
        if (updatedImagene.getImageData() != null) {
            updatedImageneDTO.setImageDataBase64(Base64.getEncoder().encodeToString(updatedImagene.getImageData()));
        }

        return ResponseEntity.ok().body(updatedImageneDTO);
    }

    public ResponseEntity<List<ImageneDTO>> getImageneByInvitado() {
        List<Imagene> imagenes = imageneRepository.findAll();
        List<ImageneDTO> imageneDTOS = new ArrayList<>();
        for (Imagene imagene : imagenes) {
            if (imagene.getIdUsuario().getRol().equals("GUEST")) {
                ImageneDTO imageneDTO = modelMapper.map(imagene, ImageneDTO.class);

                // Convert image data to Base64 if available
                if (imagene.getImageData() != null) {
                    imageneDTO.setImageDataBase64(Base64.getEncoder().encodeToString(imagene.getImageData()));
                }

                imageneDTOS.add(imageneDTO);
            }
        }
        return ResponseEntity.ok().body(imageneDTOS);
    }

    public void uploadImagene(ImageneUploadDto imagenDTO) {
        String base64Content = imagenDTO.getContent();

        try {
            // Decodificar la cadena Base64 a bytes
            byte[] bytes = Base64.getDecoder().decode(base64Content);

            // Create a new Imagene entity
            Imagene imagene = new Imagene();
            imagene.setFecha(ZonedDateTime.now());
            imagene.setComentario(imagenDTO.getImagenDTO().getComentario());

            // Store the image data as a BLOB
            imagene.setImageData(bytes);

            // Set the Jardine reference
            if (imagenDTO.getImagenDTO().getIdJardin() != null) {
                Jardine jardine = jardineRepository.findById(imagenDTO.getImagenDTO().getIdJardin())
                    .orElseThrow(() -> new ResourceNotFoundException("Jardine not found with id " + imagenDTO.getImagenDTO().getIdJardin()));
                imagene.setIdJardin(jardine);
            }

            // Set the Usuario reference
            if (imagenDTO.getImagenDTO().getIdUsuario() != null) {
                Usuario usuario = usuarioRepository.findById(imagenDTO.getImagenDTO().getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + imagenDTO.getImagenDTO().getIdUsuario()));
                imagene.setIdUsuario(usuario);
            }

            // Create and set the Document reference
            Documentos documento = new Documentos();
            documento.setName(imagenDTO.getFileName());
            documento.setIsFolder(false);
            documento.setFecha(ZonedDateTime.now());

            // Set the user from the image
            if (imagene.getIdUsuario() != null) {
                documento.setIdCliente(imagene.getIdUsuario());
            }

            // Save the document first
            Documentos savedDocumento = documentRepository.save(documento);

            // Set the saved document in the image
            imagene.setIdDocument(savedDocumento);

            // Save the image
            imageneRepository.save(imagene);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage(), e);
        }
    }


    public void deleteImagene(Integer id) {
        Imagene imagene = imageneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Imagene not found with id " + id));
        imageneRepository.delete(imagene);
    }
}
