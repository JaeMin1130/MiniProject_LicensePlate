package plate.back.domain.record.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import plate.back.domain.history.service.HistoryService;
import plate.back.domain.record.dto.MultiResponseDto;
import plate.back.domain.record.dto.RecordRequestDto;
import plate.back.domain.record.dto.RecordResponseDto;
import plate.back.domain.record.service.RecordService;
import plate.back.global.flask.dto.FlaskResponseDto;
import plate.back.global.flask.service.FlaskService;
import plate.back.global.s3.service.FileService;
import plate.back.global.utils.Base64ToMultipartFileConverter;

@RequiredArgsConstructor
@RequestMapping("/api/records")
@RestController
public class RecordController {

    private final RecordService recordService;
    private final FlaskService flaskService;
    private final HistoryService historyService;
    private final FileService fileService;

    // 3. 차량 출입 로그 기록
    @PostMapping
    public ResponseEntity<MultiResponseDto> recordLog(MultipartFile vehicleFile) throws IOException {

        FlaskResponseDto flaskResponseDto = flaskService.callApi(vehicleFile).getBody();

        Map<String, String> vehicleImgMap = fileService.uploadFile(vehicleFile, 0);

        MultipartFile plateFile = Base64ToMultipartFileConverter
                .convertBase64ToMultipartFile(flaskResponseDto.getPlateImg());
        Map<String, String> plateImgMap = fileService.uploadFile(plateFile, 1);

        MultiResponseDto responseDto = recordService.recordLog(flaskResponseDto, vehicleImgMap, plateImgMap);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 4. 날짜별 로그 조회 yy-MM-dd
    @GetMapping("/date/{start}/{end}")
    public ResponseEntity<List<RecordResponseDto>> searchDate(@PathVariable String start, @PathVariable String end)
            throws ParseException {

        List<RecordResponseDto> list = recordService.searchDate(start, end);
        return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    // 5. 차량 번호별 로그 조회
    @GetMapping("/plate/{plate}")
    public ResponseEntity<List<RecordResponseDto>> searchPlate(@PathVariable String plate) {

        List<RecordResponseDto> list = recordService.searchPlate(plate);
        return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    // 6. 로그 수정(admin)
    @PutMapping
    public ResponseEntity<?> updateRecord(@RequestBody ArrayList<RecordRequestDto.Update> list,
            @AuthenticationPrincipal String memberId) throws IOException {

        for (RecordRequestDto.Update resqDto : list) {
            String previousText = recordService.updateRecord(resqDto);
            historyService.createHistory(resqDto, previousText, memberId, "update");
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 7. 로그 삭제(admin)
    @DeleteMapping
    public ResponseEntity<?> deleteRecord(@RequestBody ArrayList<RecordRequestDto.Delete> list,
            @AuthenticationPrincipal String memberId) {

        for (RecordRequestDto.Delete resqDto : list) {
            recordService.deleteRecord(resqDto);
            historyService.createHistory(resqDto, memberId);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}