package plate.back.global.jwt.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                        FilterChain filterChain)
                        throws ServletException, IOException {

                // 요청에서 토큰 가져오기.
                String token = tokenService.parseBearerToken(request);

                log.info("Filter is running...");

                // 토큰 검사하기. JWT이므로 인증 서버에 요청 하지 않고도 검증 가능.
                DecodedJWT decodedJwt = tokenService.verifyToken(token);

                String memberId = decodedJwt.getSubject();
                String memberRole = decodedJwt.getClaim("role").asString(); // toString()은 따옴표까지 포함된다.
                
                List<GrantedAuthority> memberRoleList = AuthorityUtils.createAuthorityList(memberRole);

                // 인증 완료 후 Authentication 인스턴스를 만들어 SecurityContextHolder에 등록

                // principal 필드 타입이 Object라서 아무거나 넣어도 되지만, 통상적으로 UserDetails 인스턴스를 넣는다.
                UserDetails userDetails = new User(memberId, "null", memberRoleList);
                
                // Authentication(interface)> AbstractAuthenticationToken > UsernamePasswordAuthenticationToken
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "null", memberRoleList);

                log.info("Authentication : " + authentication.toString());

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                                
                SecurityContextHolder.setContext(securityContext);

                filterChain.doFilter(request, response);

        }

}
