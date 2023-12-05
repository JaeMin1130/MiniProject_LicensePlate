package plate.back.domain.history.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import plate.back.domain.history.dto.HistoryResponseDto;
import plate.back.domain.history.service.HistoryService;

@Tag(name = "History API")
@RequiredArgsConstructor
@RequestMapping("/api/histories")
@RestController
public class HistoryController {

    private final HistoryService historyService;

    // 8. 수정/삭제 기록 조회
    @Operation(summary = "수정/삭제 기록 조회")
    @GetMapping
    public ResponseEntity<List<HistoryResponseDto>> getHistory() {

        List<HistoryResponseDto> list = historyService.getHistory();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}