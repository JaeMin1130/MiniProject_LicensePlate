package plate.back.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import plate.back.domain.member.entity.Role;
import plate.back.domain.refreshToken.service.TokenService;
import plate.back.global.jwt.filter.JwtAuthenticationFilter;
import plate.back.global.jwt.filter.JwtExceptionFilter;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenService tokenService;

    /*
     * to do : 이 방법은 Spring Security의 다른 보안 요소도 싸그리 무시해서 권장되지 않는다고 한다.
     * 필터체인을 추가로 생성해서 하는 방법으로 바꾸면 좋다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("swagger-ui/**")
                .requestMatchers("v3/api-docs/**")
                .requestMatchers(HttpMethod.POST, "/api/members/**")
                .requestMatchers(HttpMethod.POST, "/api/reissue/accessToken");
    }
    
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF(Cross-Site Request Forgery) 보호 비활성화
                .httpBasic(httpbasic -> httpbasic.disable()) // Basic 인증 비활성화
                .formLogin(formLogin -> formLogin.disable()) // 폼 기반 로그인 비활성화
                .logout(logout -> logout.disable()) // 로그아웃 처리 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/members/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/records").permitAll()
                            .requestMatchers("/api/records/plate/**", "/api/records/date/**").authenticated()
                            .requestMatchers("/api/histories").hasRole(Role.ADMIN.toString())
                            .requestMatchers(HttpMethod.DELETE, "/api/records").hasRole(Role.ADMIN.toString())
                            .requestMatchers(HttpMethod.PUT, "/api/records").hasRole(Role.ADMIN.toString());
                    // auth.requestMatchers("/**").permitAll();
                })
                .addFilterBefore(new JwtAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }

    // 암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}