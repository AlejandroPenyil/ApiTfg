package TFG.Terranaturale.Service;

import TFG.Terranaturale.Repository.ConversacionRepository;
import TFG.Terranaturale.Repository.MensajeRepository;
import TFG.Terranaturale.Repository.UsuarioRepository;
import TFG.Terranaturale.model.Dto.ConversacionDTO;
import TFG.Terranaturale.model.Dto.MensajeDTO;
import TFG.Terranaturale.model.Entity.Conversacion;
import TFG.Terranaturale.model.Entity.Mensaje;
import TFG.Terranaturale.model.Entity.Usuario;
import TFG.Terranaturale.model.Exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MensajeriaService {

    private final ConversacionRepository conversacionRepository;
    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public MensajeriaService(ConversacionRepository conversacionRepository,
                            MensajeRepository mensajeRepository,
                            UsuarioRepository usuarioRepository,
                            ModelMapper modelMapper) {
        this.conversacionRepository = conversacionRepository;
        this.mensajeRepository = mensajeRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Get all conversations (only accessible to administrators)
     */
    public ResponseEntity<List<ConversacionDTO>> getAllConversaciones() {
        List<Conversacion> conversaciones = conversacionRepository.findAll();
        List<ConversacionDTO> conversacionDTOs = conversaciones.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(conversacionDTOs);
    }

    /**
     * Get a conversation by ID
     */
    public ResponseEntity<ConversacionDTO> getConversacionById(Integer id) {
        Conversacion conversacion = conversacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con id " + id));
        return ResponseEntity.ok(convertToDTO(conversacion));
    }

    /**
     * Get a user's conversation
     */
    public ResponseEntity<ConversacionDTO> getConversacionByUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId));
        
        Optional<Conversacion> conversacionOpt = conversacionRepository.findByIdUsuario(usuario);
        
        if (conversacionOpt.isPresent()) {
            return ResponseEntity.ok(convertToDTO(conversacionOpt.get()));
        } else {
            // Create a new conversation for the user if one doesn't exist
            Conversacion nuevaConversacion = new Conversacion();
            nuevaConversacion.setIdUsuario(usuario);
            nuevaConversacion.setFechaCreacion(ZonedDateTime.now());
            nuevaConversacion.setTitulo("Conversación con " + usuario.getNombre() + " " + usuario.getApellidos());
            
            Conversacion savedConversacion = conversacionRepository.save(nuevaConversacion);
            return ResponseEntity.ok(convertToDTO(savedConversacion));
        }
    }

    /**
     * Send a message
     */
    @Transactional
    public ResponseEntity<MensajeDTO> enviarMensaje(MensajeDTO mensajeDTO) {
        // Validate the conversation exists
        Conversacion conversacion = conversacionRepository.findById(mensajeDTO.getIdConversacion())
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con id " + mensajeDTO.getIdConversacion()));
        
        // Validate the sender exists
        Usuario emisor = usuarioRepository.findById(mensajeDTO.getIdEmisor())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + mensajeDTO.getIdEmisor()));
        
        // Create and save the message
        Mensaje mensaje = new Mensaje();
        mensaje.setIdConversacion(conversacion);
        mensaje.setIdEmisor(emisor);
        mensaje.setContenido(mensajeDTO.getContenido());
        mensaje.setFechaEnvio(ZonedDateTime.now());
        mensaje.setLeido(false);
        
        Mensaje savedMensaje = mensajeRepository.save(mensaje);
        return ResponseEntity.ok(convertToDTO(savedMensaje));
    }

    /**
     * Mark messages as read
     */
    @Transactional
    public ResponseEntity<Void> marcarComoLeidos(Integer conversacionId, Integer usuarioId) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con id " + conversacionId));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId));
        
        // Only mark messages as read if they were sent by someone else
        List<Mensaje> mensajesNoLeidos = mensajeRepository.findByIdConversacionAndLeidoFalseOrderByFechaEnvioAsc(conversacion);
        for (Mensaje mensaje : mensajesNoLeidos) {
            if (!mensaje.getIdEmisor().getId().equals(usuario.getId())) {
                mensaje.setLeido(true);
                mensajeRepository.save(mensaje);
            }
        }
        
        return ResponseEntity.ok().build();
    }

    /**
     * Convert a Conversacion entity to a ConversacionDTO
     */
    private ConversacionDTO convertToDTO(Conversacion conversacion) {
        ConversacionDTO dto = modelMapper.map(conversacion, ConversacionDTO.class);
        
        // Add user information
        dto.setIdUsuario(conversacion.getIdUsuario().getId());
        dto.setNombreUsuario(conversacion.getIdUsuario().getNombre() + " " + conversacion.getIdUsuario().getApellidos());
        
        // Add messages
        List<Mensaje> mensajes = mensajeRepository.findByIdConversacionOrderByFechaEnvioDesc(conversacion);
        List<MensajeDTO> mensajeDTOs = new ArrayList<>();
        
        for (Mensaje mensaje : mensajes) {
            mensajeDTOs.add(convertToDTO(mensaje));
        }
        
        dto.setMensajes(mensajeDTOs);
        return dto;
    }

    /**
     * Convert a Mensaje entity to a MensajeDTO
     */
    private MensajeDTO convertToDTO(Mensaje mensaje) {
        MensajeDTO dto = modelMapper.map(mensaje, MensajeDTO.class);
        
        // Add sender information
        dto.setIdEmisor(mensaje.getIdEmisor().getId());
        dto.setNombreEmisor(mensaje.getIdEmisor().getNombre() + " " + mensaje.getIdEmisor().getApellidos());
        dto.setRolEmisor(mensaje.getIdEmisor().getRol());
        
        return dto;
    }
}