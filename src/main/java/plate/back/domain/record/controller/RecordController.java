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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Record API", description = "차량 출입 기록 CRUD")
@RequiredArgsConstructor
@RequestMapping("/api/records")
@RestController
public class RecordController {

    private final RecordService recordService;
    private final FlaskService flaskService;
    private final HistoryService historyService;
    private final FileService fileService;

    @Operation(summary = "차량 출입 기록 저장", description = "Flask 서버로 이미지 전달, AI 모델이 번호판 예측, S3에 이미지(차량, 번호판) 저장, 가장 정확도 높은 예측값 Record 인스턴스로 저장")
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

    @Operation(summary = "날짜별 기록 조회", description = "\"yy-MM-dd\" 형식의 날짜를 받아 기록 조회")
    @GetMapping("/date/{start}/{end}")
    public ResponseEntity<List<RecordResponseDto>> searchDate(@PathVariable String start, @PathVariable String end)
            throws ParseException {

        List<RecordResponseDto> list = recordService.searchDate(start, end);
        return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    @Operation(summary = "차량 번호별 기록 조회", description = "차량 번호를 받아 기록을 조회")
    @GetMapping("/plate/{plate}")
    public ResponseEntity<List<RecordResponseDto>> searchPlate(@PathVariable String plate) {

        List<RecordResponseDto> list = recordService.searchPlate(plate);
        return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    @Operation(summary = "차량 출입 기록 수정(ADMIN)", description = "AI 모델이 예측한 값이 틀린 경우, 관리자가 확인 후 수정, S3에 저장된 번호판 이미지 삭제, 재학습을 위해 차량 이미지 relearn 폴더로 이동")
    @PutMapping
    public ResponseEntity<?> updateRecord(@RequestBody ArrayList<RecordRequestDto.Update> list,
            @AuthenticationPrincipal String memberId) throws IOException {

        for (RecordRequestDto.Update resqDto : list) {
            String previousText = recordService.updateRecord(resqDto);
            historyService.createHistory(resqDto, previousText, memberId, "update");
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "차량 출입 기록 삭제(ADMIN)", description = "Record 인스턴스 삭제(연결된 Image, PredictedPlate도 삭제됨), S3에 저장된 이미지 삭제")
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