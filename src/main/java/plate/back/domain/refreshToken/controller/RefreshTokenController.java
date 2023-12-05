package plate.back.domain.refreshToken.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import plate.back.domain.refreshToken.dto.ReissueResponseDto;
import plate.back.domain.refreshToken.service.TokenService;

@Tag(name = "RefreshToken API")
@RequiredArgsConstructor
@RestController
public class RefreshTokenController {
    
    private final TokenService refreshTokenService;

    @Operation(summary = "Access Token 재발행", description = "Request header의 Refresh Token 검증 후, Access Token 재발행")
    @PostMapping("/api/reissue")
    public ResponseEntity<ReissueResponseDto> reIssueAccessToken(@RequestHeader("RefreshToken") String refreshToken){

        ReissueResponseDto respDto = refreshTokenService.reissueAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(respDto);
    }
}
