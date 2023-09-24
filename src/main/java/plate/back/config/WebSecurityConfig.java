package plate.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import plate.back.jwt.JwtAuthorizationFilter;
import plate.back.jwt.JwtExceptionHandler;
import plate.back.jwt.JwtTokenProvider;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtExceptionHandler exceptionHandler;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).httpBasic(httpbasic -> httpbasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/main/**").permitAll()
                            .requestMatchers("/user/signin").permitAll()
                            .requestMatchers("/user/signup").permitAll()
                            .requestMatchers("main/search/plate/**", "main/search/date/**", "/main/record")
                            .hasAnyRole("ADMIN", "USER")
                            .requestMatchers("/main/update", "/main/delete", "main/history").hasRole("ADMIN")
                            .anyRequest().authenticated();
                    // auth.requestMatchers("/**").permitAll();
                })
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider, exceptionHandler),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}