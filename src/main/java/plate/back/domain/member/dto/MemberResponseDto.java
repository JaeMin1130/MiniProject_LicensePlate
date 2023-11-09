package plate.back.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {

    private String accessToken;

    public static MemberResponseDto of(String accessToken) {
        return MemberResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
