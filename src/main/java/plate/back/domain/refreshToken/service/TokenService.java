package plate.back.domain.refreshToken.service;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletRequest;
import plate.back.domain.member.entity.Member;
import plate.back.domain.refreshToken.dto.ReissueResponseDto;

public interface TokenService {

    public String createAccessToken(Member member);
    public String createRefreshToken(Member member);

    public ReissueResponseDto reissueAccessToken(String refreshToken);

    public DecodedJWT verifyToken(String token);

    public String parseBearerToken(String bearerToken);
    public String parseBearerToken(HttpServletRequest request);
    
}
