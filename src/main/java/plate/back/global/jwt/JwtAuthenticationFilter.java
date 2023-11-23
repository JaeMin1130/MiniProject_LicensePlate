package plate.back.global.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            // 요청에서 토큰 가져오기.
            String token = parseBearerToken(request);
            log.info("Filter is running...");
            // 토큰 검사하기. JWT이므로 인증 서버에 요청 하지 않고도 검증 가능.
            JWTVerifier verifier = JWT.require(JwtProperties.ALGORITHM).build();
            DecodedJWT decodedJwt = verifier.verify(token);
            // log.info("Authenticated user ID : " + userId);
            String memberId = decodedJwt.getSubject();
            String memberRole = decodedJwt.getClaim("role").toString();

            // 인증 완료; SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberId, null,
                    AuthorityUtils.createAuthorityList(memberRole));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

        } catch (TokenExpiredException tokenExpiredException) {

        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String Jwt = request.getHeader("Authorization");

        if (StringUtils.hasText(Jwt) && Jwt.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return Jwt.substring(7);
        }
        return null;
    }
}
