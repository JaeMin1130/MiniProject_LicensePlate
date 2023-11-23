// package plate.back.global.exception.handler;

// import java.io.IOException;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.stereotype.Component;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.MalformedJwtException;
// import io.jsonwebtoken.UnsupportedJwtException;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.Getter;
// import lombok.ToString;

// @Component
// public class JwtExceptionHandler {

// public void handleException(HttpServletRequest request, HttpServletResponse
// response, Exception e)
// throws ServletException, IOException {
// ObjectMapper objectMapper = new ObjectMapper();
// response.setContentType(MediaType.APPLICATION_JSON_VALUE);
// if (e instanceof SecurityException || e instanceof MalformedJwtException) {
// objectMapper.writeValue(response.getWriter(),
// new ErrorResponse("Unauthorized", e.getMessage()));
// } else if (e instanceof ExpiredJwtException) {
// objectMapper.writeValue(response.getWriter(),
// new ErrorResponse("Token Expired", e.getMessage()));
// } else if (e instanceof UnsupportedJwtException) {
// objectMapper.writeValue(response.getWriter(),
// new ErrorResponse("Unsupported Token", e.getMessage()));
// } else if (e instanceof IllegalArgumentException) {
// objectMapper.writeValue(response.getWriter(),
// new ErrorResponse("Illegal Argument", e.getMessage()));
// } else {
// objectMapper.writeValue(response.getWriter(),
// new ErrorResponse("Unknown JWT Exception", e.getMessage()));
// ;
// }
// }
// }

// @Getter
// @ToString
// class ErrorResponse {
// private String errorCode;
// private String errorMessage;

// public ErrorResponse(String errorCode, String errorMessage) {
// this.errorCode = errorCode;
// this.errorMessage = errorMessage;
// }
// }
