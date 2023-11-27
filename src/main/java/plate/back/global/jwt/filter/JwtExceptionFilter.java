package plate.back.global.jwt.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import plate.back.global.exception.CustomException;
import plate.back.global.exception.ErrorCode;
import plate.back.global.exception.ErrorResponseDto;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {

            CustomException customException = new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(customException.getErrorCode().getStatus().value());
            
            objectMapper.writeValue(response.getWriter(), ErrorResponseDto.of(customException));
        } catch (JWTVerificationException e) {

            CustomException customException = new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(customException.getErrorCode().getStatus().value());
            
            objectMapper.writeValue(response.getWriter(), ErrorResponseDto.of(customException));
        }
    }
}
