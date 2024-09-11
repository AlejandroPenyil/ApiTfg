package TFG.Terranaturale.Controller;

import Dto.DocumentDto;
import TFG.Terranaturale.Service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentDto>> getAllFacturas() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getFacturaById(@PathVariable Integer id) {
        DocumentDto documentDto = documentService.findById(id);
        if (documentDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(documentDto);
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createFactura(@RequestBody DocumentDto documentDto) {
        DocumentDto createdDocument = documentService.save(documentDto);
        return ResponseEntity.ok(createdDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateFactura(@PathVariable Integer id, @RequestBody DocumentDto documentDto) {
        DocumentDto existingDocumentos = documentService.findById(id);
        if (existingDocumentos == null) {
            return ResponseEntity.notFound().build();
        }
        documentDto.setId(id);
        DocumentDto updatedDocument = documentService.save(documentDto);
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Integer id) {
        DocumentDto existingDocument  = documentService.findById(id);
        if (existingDocument == null) {
            return ResponseEntity.notFound().build();
        }
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
