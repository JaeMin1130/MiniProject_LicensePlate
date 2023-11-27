package plate.back.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {

    private String accessToken;
    private String refreshToken;

    public static MemberResponseDto of(String accessToken, String refreshToken) {

        return MemberResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}
