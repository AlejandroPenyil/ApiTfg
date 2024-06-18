package TFG.Terranaturale.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageneUploadDto {
    private String fileName;
    private String fileType;
    private String content;
    private ImageneDTO imagenDTO;
}
