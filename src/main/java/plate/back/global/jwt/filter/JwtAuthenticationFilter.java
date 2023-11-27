package plate.back.global.jwt.filter;

import java.io.IOException;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import plate.back.domain.refreshToken.service.TokenService;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            // 요청에서 토큰 가져오기.
            String token = tokenService.parseBearerToken(request);

            log.info("Filter is running...");

            // 토큰 검사하기. JWT이므로 인증 서버에 요청 하지 않고도 검증 가능.
            DecodedJWT decodedJwt = tokenService.verifyToken(token);

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

            filterChain.doFilter(request, response);

        }

}
