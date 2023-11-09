package plate.back.global.flask;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import plate.back.global.utils.RateLimiter;

@RequiredArgsConstructor
@Service
public class FlaskService {

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    private final RateLimiter rateLimiter;

    public ResponseEntity<?> callApi(MultipartFile file) throws IOException {
        // Check if the request is allowed by the rate limiter
        if (!rateLimiter.allowRequest()) {
            System.out.println("Too many requests");
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

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        System.out.println("Flask Connection Starts");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(
                flaskApiUrl + "/main/record", // Use the configured API URL
                HttpMethod.POST,
                requestEntity,
                Object.class);
        System.out.println("Response : " + response);
        System.out.println("Flask Connection Success");
        return response;
    }
}
