package TFG.Terranaturale.Controller;

import TFG.Terranaturale.Service.MensajeriaService;
import TFG.Terranaturale.model.Dto.ConversacionDTO;
import TFG.Terranaturale.model.Dto.MensajeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensajeria")
public class MensajeriaController {
    private static final Logger logger = LoggerFactory.getLogger(MensajeriaController.class);

    private final MensajeriaService mensajeriaService;

    public MensajeriaController(MensajeriaService mensajeriaService) {
        this.mensajeriaService = mensajeriaService;
    }

    /**
     * Get all conversations (only accessible to administrators)
     */
    @GetMapping("/conversaciones")
    public ResponseEntity<List<ConversacionDTO>> getAllConversaciones() {
        logger.info("GET request to get all conversaciones");
        ResponseEntity<List<ConversacionDTO>> response = mensajeriaService.getAllConversaciones();
        logger.info("Returning {} conversaciones", response.getBody().size());
        return response;
    }

    /**
     * Get a conversation by ID
     */
    @GetMapping("/conversaciones/{id}")
    public ResponseEntity<ConversacionDTO> getConversacionById(@PathVariable Integer id) {
        logger.info("GET request to get conversacion with id: {}", id);
        try {
            ResponseEntity<ConversacionDTO> response = mensajeriaService.getConversacionById(id);
            logger.info("Returning conversacion with id: {}", id);
            return response;
        } catch (Exception e) {
            logger.warn("Conversacion with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get a user's conversation
     */
    @GetMapping("/usuarios/{usuarioId}/conversacion")
    public ResponseEntity<ConversacionDTO> getConversacionByUsuario(@PathVariable Integer usuarioId) {
        logger.info("GET request to get conversacion for usuario with id: {}", usuarioId);
        try {
            ResponseEntity<ConversacionDTO> response = mensajeriaService.getConversacionByUsuario(usuarioId);
            logger.info("Returning conversacion for usuario with id: {}", usuarioId);
            return response;
        } catch (Exception e) {
            logger.warn("Error getting conversacion for usuario with id {}: {}", usuarioId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Send a message
     */
    @PostMapping("/mensajes")
    public ResponseEntity<MensajeDTO> enviarMensaje(@RequestBody MensajeDTO mensajeDTO) {
        logger.info("POST request to send mensaje to conversacion with id: {}", mensajeDTO.getIdConversacion());
        try {
            ResponseEntity<MensajeDTO> response = mensajeriaService.enviarMensaje(mensajeDTO);
            logger.info("Mensaje sent successfully");
            return response;
        } catch (Exception e) {
            logger.error("Error sending mensaje: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Mark messages as read
     */
    @PostMapping("/conversaciones/{conversacionId}/usuarios/{usuarioId}/leidos")
    public ResponseEntity<Void> marcarComoLeidos(@PathVariable Integer conversacionId, @PathVariable Integer usuarioId) {
        logger.info("POST request to mark mensajes as read in conversacion with id: {} for usuario with id: {}", conversacionId, usuarioId);
        try {
            ResponseEntity<Void> response = mensajeriaService.marcarComoLeidos(conversacionId, usuarioId);
            logger.info("Mensajes marked as read successfully");
            return response;
        } catch (Exception e) {
            logger.error("Error marking mensajes as read: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}