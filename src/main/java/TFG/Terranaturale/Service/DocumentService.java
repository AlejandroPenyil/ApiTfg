package TFG.Terranaturale.Service;

import TFG.Terranaturale.model.Dto.DocumentDto;
import TFG.Terranaturale.model.Entity.Documentos;
import TFG.Terranaturale.Repository.DocumentRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;

    public DocumentService(DocumentRepository documentRepository, ModelMapper modelMapper) {
        this.documentRepository = documentRepository;
        this.modelMapper = modelMapper;
    }


    public List<DocumentDto> findAll() {
        logger.info("Finding all documents");
        List<DocumentDto> documentList = documentRepository.findAll().stream()
                .map(document -> modelMapper.map(document, DocumentDto.class))
                .collect(Collectors.toList());
        logger.info("Found {} documents", documentList.size());
        return documentList;
    }

    public DocumentDto findById(Integer id) {
        logger.info("Finding document with id: {}", id);
        return documentRepository.findById(id)
                .map(document -> {
                    logger.info("Found document: {}", document.getName());
                    return modelMapper.map(document, DocumentDto.class);
                })
                .orElse(null);
    }

    public DocumentDto save(DocumentDto documentDto) {
        logger.info("Saving document: {}", documentDto.getName());
        Documentos document = modelMapper.map(documentDto, Documentos.class);
        document = documentRepository.save(document);
        logger.info("Saved document with id: {}", document.getId());
        return modelMapper.map(document, DocumentDto.class);
    }

    public void deleteById(Integer id) {
        logger.info("Deleting document with id: {}", id);
        documentRepository.deleteById(id);
        logger.info("Deleted document with id: {}", id);
    }
}
