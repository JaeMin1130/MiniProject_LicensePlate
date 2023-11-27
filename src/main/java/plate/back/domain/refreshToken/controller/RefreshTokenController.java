package plate.back.domain.refreshToken.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import plate.back.domain.refreshToken.dto.ReissueResponseDto;
import plate.back.domain.refreshToken.service.TokenService;

@RequiredArgsConstructor
@RestController
public class RefreshTokenController {
    
    private final TokenService refreshTokenService;

    @PostMapping("/api/reissue/accessToken")
    public ResponseEntity<ReissueResponseDto> reIssueAccessToken(@RequestHeader("RefreshToken") String refreshToken){

        ReissueResponseDto respDto = refreshTokenService.reissueAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(respDto);
    }
}
