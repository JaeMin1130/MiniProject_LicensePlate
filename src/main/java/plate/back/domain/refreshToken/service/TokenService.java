package plate.back.domain.refreshToken.service;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletRequest;
import plate.back.domain.member.entity.Member;
import plate.back.domain.refreshToken.dto.ReissueResponseDto;

public interface TokenService {

    // 액세스 토큰 생성
    public String createAccessToken(Member member);

    // 리프레쉬 토큰 생성, DB에 저장
    public String createRefreshToken(Member member);

    // 리프레쉬 토큰 검증 후 액세스 토큰 재발행
    public ReissueResponseDto reissueAccessToken(String refreshToken);

    // 토큰 검증
    public DecodedJWT verifyToken(String token);

    // 토큰에서 JwtProperties.TOKEN_PREFIX("Bearer ") 제거
    public String parseBearerToken(String bearerToken);
    public String parseBearerToken(HttpServletRequest request);
    
}
