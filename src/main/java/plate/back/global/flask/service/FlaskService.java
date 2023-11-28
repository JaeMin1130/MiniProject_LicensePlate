package plate.back.global.flask.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import plate.back.global.flask.dto.FlaskResponseDto;

public interface FlaskService {

    public ResponseEntity<FlaskResponseDto> callApi(MultipartFile file) throws IOException;

}
