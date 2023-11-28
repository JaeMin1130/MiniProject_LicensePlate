package plate.back.global.flask.service;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import plate.back.global.flask.dto.FlaskResponseDto;
import plate.back.global.utils.RateLimiter;

@Slf4j
@RequiredArgsConstructor
@Service
public class FlaskServiceImpl implements FlaskService{

    private final RateLimiter rateLimiter;
    private final WebClient webClient = WebClient.builder().baseUrl("http://1.252.90.210:5000").build();

    @Override
    public ResponseEntity<FlaskResponseDto> callApi(MultipartFile file) throws IOException {
        // Check if the request is allowed by the rate limiter
        if (!rateLimiter.allowRequest()) {
            log.error("Too many requests");
            // return response.fail(HttpStatus.TOO_MANY_REQUESTS);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        log.info("Flask Connection Starts");

        ResponseEntity<FlaskResponseDto> response = webClient.post()
                .uri("/api/records")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .toEntity(FlaskResponseDto.class)
                .block(); // block until the response is available

        log.info("Response : " + response);
        log.info("Flask Connection Success");
        return response;
    }
}
