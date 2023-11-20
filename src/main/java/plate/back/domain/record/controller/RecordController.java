package plate.back.domain.record.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import plate.back.domain.history.dto.HistoryResponseDto;
import plate.back.domain.record.dto.MultiResponseDto;
import plate.back.domain.record.dto.RecordResponseDto;
import plate.back.domain.record.service.RecordService;

@RequiredArgsConstructor
@RequestMapping("/api/records")
@RestController
public class RecordController {

    private final RecordService recordService;

    // 3. 차량 출입 로그 기록
    @PostMapping
    public ResponseEntity<MultiResponseDto> recordLog(MultipartFile file) throws IOException {
        MultiResponseDto responseDto = recordService.recordLog(file);
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
    public ResponseEntity<?> searchPlate(@PathVariable String plate) {
        List<RecordResponseDto> list = recordService.searchPlate(plate);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    // 6. 수정/삭제 기록 조회
    @GetMapping("/history")
    public ResponseEntity<?> getHistory() {
        List<HistoryResponseDto> list = recordService.getHistory();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    // 7. 로그 수정(admin)
    @PutMapping
    public ResponseEntity<?> updateLog(@RequestBody ArrayList<RecordResponseDto> list) throws IOException {
        recordService.updateLog(list);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 8. 로그 삭제(admin)
    @DeleteMapping
    public ResponseEntity<?> deleteLog(@RequestBody ArrayList<RecordResponseDto> list) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}