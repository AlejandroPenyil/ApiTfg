package TFG.Terranaturale.model.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUpload {
    private String fileName;
    private String fileType;
    private String content;
    private Integer id;
}
