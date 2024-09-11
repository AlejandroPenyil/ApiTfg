package TFG.Terranaturale.Service;

import Dto.DocumentDto;
import Dto.FacturaDTO;
import Entity.Documentos;
import Entity.Factura;
import TFG.Terranaturale.Repository.DocumentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;

    public DocumentService(DocumentRepository documentRepository, ModelMapper modelMapper) {
        this.documentRepository = documentRepository;
        this.modelMapper = modelMapper;
    }


    public List<DocumentDto> findAll() {
        return documentRepository.findAll().stream()
                .map(documentos -> modelMapper.map(documentos, DocumentDto.class))
                .collect(Collectors.toList());
    }

    public DocumentDto findById(Integer id) {
        return documentRepository.findById(id)
                .map(documentos -> modelMapper.map(documentos, DocumentDto.class))
                .orElse(null);
    }

    public DocumentDto save(DocumentDto documentDto) {
        Documentos documentos = modelMapper.map(documentDto, Documentos.class);
        documentos = documentRepository.save(documentos);
        return documentDto.convertToDto(documentos);
    }

    public void deleteById(Integer id) {
        documentRepository.deleteById(id);
    }
}
