package TFG.Terranaturale.Controller;

import TFG.Terranaturale.model.Dto.DocumentDto;
import TFG.Terranaturale.Service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        logger.info("GET request to get all documents");
        List<DocumentDto> documents = documentService.findAll();
        logger.info("Returning {} documents", documents.size());
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Integer id) {
        logger.info("GET request to get document with id: {}", id);
        DocumentDto documentDto = documentService.findById(id);
        if (documentDto == null) {
            logger.warn("Document with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Returning document with id: {}", id);
        return ResponseEntity.ok(documentDto);
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody DocumentDto documentDto) {
        logger.info("POST request to create document: {}", documentDto.getName());
        DocumentDto createdDocument = documentService.save(documentDto);
        logger.info("Created document with id: {}", createdDocument.getId());
        return ResponseEntity.ok(createdDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Integer id, @RequestBody DocumentDto documentDto) {
        logger.info("PUT request to update document with id: {}", id);
        DocumentDto existingDocument = documentService.findById(id);
        if (existingDocument == null) {
            logger.warn("Document with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
        documentDto.setId(id);
        DocumentDto updatedDocument = documentService.save(documentDto);
        logger.info("Updated document with id: {}", updatedDocument.getId());
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Integer id) {
        logger.info("DELETE request to delete document with id: {}", id);
        DocumentDto existingDocument = documentService.findById(id);
        if (existingDocument == null) {
            logger.warn("Document with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
        documentService.deleteById(id);
        logger.info("Deleted document with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
