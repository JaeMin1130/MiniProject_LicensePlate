package plate.back.domain.history.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import plate.back.domain.history.dto.HistoryResponseDto;
import plate.back.domain.history.entity.History;
import plate.back.domain.history.entity.TaskType;
import plate.back.domain.history.repository.HistoryRepository;
import plate.back.domain.record.dto.RecordRequestDto;

@RequiredArgsConstructor
@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepo;

    @Override
    public List<HistoryResponseDto> getHistory() {

        List<History> entities = historyRepo.findAll();

        List<HistoryResponseDto> list = new ArrayList<>();

        for (History entity : entities) {
            list.add(HistoryResponseDto.builder()
                    .historyId(entity.getHistoryId())
                    .recordId(entity.getRecordId())
                    .memberId(entity.getMemberId())
                    .taskType(entity.getTaskType())
                    .currentText(entity.getCurrentText())
                    .previousText(entity.getPreviousText())
                    .createdDate(entity.getCreatedDate())
                    .build());
        }

        return list;
    }

    @Override
    public void createHistory(RecordRequestDto.Update resqDto, String previousText, String memberId, String taskType) {

        historyRepo.save(History.builder()
                .recordId(resqDto.getRecordId())
                .previousText(previousText)
                .currentText(resqDto.getLicensePlate())
                .taskType(TaskType.UPDATE)
                .memberId(memberId).build());

    }

    @Override
    public void createHistory(RecordRequestDto.Delete resqDto, String memberId) {

        historyRepo.save(History.builder()
                .recordId(resqDto.getRecordId())
                .previousText("delete")
                .currentText("delete")
                .taskType(TaskType.DELETE)
                .memberId(memberId).build());

    }

}
