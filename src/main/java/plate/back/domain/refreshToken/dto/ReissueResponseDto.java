package plate.back.domain.refreshToken.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueResponseDto {
    private String accessToken;
        
}
