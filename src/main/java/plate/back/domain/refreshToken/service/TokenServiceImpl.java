package plate.back.domain.refreshToken.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import plate.back.domain.member.entity.Member;
import plate.back.domain.refreshToken.dto.ReissueResponseDto;
import plate.back.domain.refreshToken.entity.RefreshToken;
import plate.back.domain.refreshToken.repository.RefreshTokenRepository;
import plate.back.global.exception.CustomException;
import plate.back.global.exception.ErrorCode;
import plate.back.global.jwt.JwtProperties;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshTokenRepo;

    public String createAccessToken(Member member) {

        Date validity = new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_ACCESS);

        String accessToken = JWT.create()
                .withSubject(member.getMemberId())
                .withClaim("role", member.getRole().getValue())
                .withExpiresAt(validity)
                .sign(JwtProperties.ALGORITHM);

        return JwtProperties.TOKEN_PREFIX + accessToken;
    }

    public String createRefreshToken(Member member) {
        
        String memberId = member.getMemberId();

        if(refreshTokenRepo.existsById(memberId)){
            refreshTokenRepo.deleteById(memberId);
        }

        Date validity = new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_REFRESH);

        String refreshToken = JWT.create()
                .withSubject(memberId)
                .withExpiresAt(validity)
                .sign(JwtProperties.ALGORITHM);

        refreshToken = JwtProperties.TOKEN_PREFIX + refreshToken;

        refreshTokenRepo.save(RefreshToken.builder().member(member).value(refreshToken).build());

        return refreshToken;
    }

    @Override
    public ReissueResponseDto reissueAccessToken(String refreshToken) {

        log.info("리프레시 토큰 : " + refreshToken);

        // refreshToken 유효성 검증
        RefreshToken savedRefreshToken = refreshTokenRepo.findByValue(refreshToken)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        String parsedToken = parseBearerToken(savedRefreshToken.getValue());

        try {
            DecodedJWT decodedJwt = verifyToken(parsedToken);

        } catch (TokenExpiredException e) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);

        }

        // accessToken 재발급
        String reissuedAccessToken = createAccessToken(savedRefreshToken.getMember());

        return ReissueResponseDto.builder().accessToken(reissuedAccessToken).build();
    }

    @Override
    public DecodedJWT verifyToken(String token) {

        JWTVerifier verifier = JWT.require(JwtProperties.ALGORITHM).build();

        DecodedJWT decodedJwt = verifier.verify(token);

        return decodedJwt;
    }

    @Override
    public String parseBearerToken(String bearerToken) {

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
            
        }else{
            throw new CustomException(ErrorCode.INVALID_TOKEN_FORMAT);
        }

    }

    @Override
    public String parseBearerToken(HttpServletRequest request) {

        String Jwt = request.getHeader("Authorization");

        if (StringUtils.hasText(Jwt) && Jwt.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return Jwt.substring(7);
        }

        return null;
    }
}
