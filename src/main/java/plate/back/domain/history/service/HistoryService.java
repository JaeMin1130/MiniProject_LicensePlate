package plate.back.domain.history.service;

import java.util.List;

import plate.back.domain.record.dto.RecordRequestDto;
import plate.back.domain.history.dto.HistoryResponseDto;

public interface HistoryService {

    public List<HistoryResponseDto> getHistory();

    public void createUpdateHistory(RecordRequestDto.Update resqDto, String currentText, String memberId, String taskType);

    public void createDeleteHistory(RecordRequestDto.Delete resqDto, String memberId);
    
}
