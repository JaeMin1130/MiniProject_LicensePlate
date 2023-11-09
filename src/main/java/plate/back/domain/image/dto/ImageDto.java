package plate.back.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ImageDto {
    private Integer recordId;
    private String imageUrl;
    private String imageType;
}
