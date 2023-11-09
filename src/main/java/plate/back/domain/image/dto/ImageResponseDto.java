package plate.back.domain.image.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponseDto {
    private Integer recordId;
    private String imageUrl;
    private String imageType;

    public static ImageResponseDto of(Integer recordId, String imageUrl, String imageType) {
        return ImageResponseDto.builder()
                .recordId(recordId)
                .imageUrl(imageUrl)
                .imageType(imageType)
                .build();
    }
}
