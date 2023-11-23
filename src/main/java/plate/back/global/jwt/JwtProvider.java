package plate.back.global.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import plate.back.domain.member.entity.Member;

@Component
public class JwtProvider {

    public static String createAccessToken(Member member) {

        Date validity = new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_ACCESS);

        String accessToken = JWT.create()
                .withSubject(member.getMemberId())
                .withClaim("role", member.getRole().toString())
                .withExpiresAt(validity)
                .sign(JwtProperties.ALGORITHM);

        return JwtProperties.TOKEN_PREFIX + accessToken;
    }

    public static String createRefreshToken(Member member) {

        Date validity = new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_REFRESH);

        String refreshToken = JWT.create()
                .withSubject(member.getMemberId())
                .withExpiresAt(validity)
                .sign(JwtProperties.ALGORITHM);

        return JwtProperties.TOKEN_PREFIX + refreshToken;
    }
}
