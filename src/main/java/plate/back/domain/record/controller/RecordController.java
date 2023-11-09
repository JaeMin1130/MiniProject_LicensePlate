package plate.back.domain.record.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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
import plate.back.domain.record.dto.RecordDto;
import plate.back.domain.record.service.RecordService;
import plate.back.global.response.ResponseDto;
import plate.back.global.utils.Helper;

@RequiredArgsConstructor
@RequestMapping("/api/records")
@RestController
public class RecordController {

    private final RecordService logService;
    private final ResponseDto response;

    // 3. 차량 출입 로그 기록
    @PostMapping
    public ResponseEntity<?> recordLog(MultipartFile file) throws IOException {
        return logService.recordLog(file);
    }

    // 4. 날짜별 로그 조회 yy-MM-dd
    @GetMapping("/date/{start}/{end}")
    public ResponseEntity<?> searchDate(@PathVariable String start, @PathVariable String end) throws ParseException {
        return logService.searchDate(start, end);
    }

    // 5. 차량 번호별 로그 조회
    @GetMapping("/plate/{plate}")
    public ResponseEntity<?> searchPlate(@PathVariable String plate) {
        return logService.searchPlate(plate);
    }

    // 6. 수정/삭제 기록 조회
    @GetMapping("/history")
    public ResponseEntity<?> getHistory() {
        return logService.getHistory();
    }

    // 7. 로그 수정(admin)
    @PutMapping
    public ResponseEntity<?> updateLog(@RequestBody ArrayList<RecordDto> list, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return logService.updateLog(list);
    }

    // 8. 로그 삭제(admin)
    @DeleteMapping
    public ResponseEntity<?> deleteLog(@RequestBody ArrayList<RecordDto> list, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return logService.deleteLog(list);
    }
}