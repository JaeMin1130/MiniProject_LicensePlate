package plate.back.domain.history.service;

import java.util.List;

import plate.back.domain.record.dto.RecordRequestDto;
import plate.back.domain.history.dto.HistoryResponseDto;

public interface HistoryService {

    public List<HistoryResponseDto> getHistory();

    public void createHistory(RecordRequestDto.Update resqDto, String currentText, String memberId, String taskType);

    // 오버로딩
    public void createHistory(RecordRequestDto.Delete resqDto, String memberId);
}
