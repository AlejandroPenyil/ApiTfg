package TFG.Terranaturale.model.Dto;

import TFG.Terranaturale.model.Entity.Documentos;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class DocumentDto {
    private Integer id;
    private Boolean isFolder;
    private String name;
    private ZonedDateTime fecha;
    private Integer idCliente;
    private Integer idParent;

    public DocumentDto convertToDto(Documentos documentos){
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(documentos.getId());
        documentDto.setName(documentos.getName());
        documentDto.setFecha(documentos.getFecha());
        documentDto.setIdCliente(documentos.getIdCliente().getId());
        documentDto.setIsFolder(documentos.getIsFolder());
        if(documentos.getIdParent() != null) {
            documentDto.setIdParent(documentos.getIdParent().getId());
        }
        return documentDto;
    }
}
