package plate.back.domain.history.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import plate.back.domain.history.entity.TaskType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryResponseDto {
    private Integer historyId;
    private Integer recordId;
    private String memberId;
    private TaskType taskType;
    private String previousText;
    private String currentText;
    private LocalDateTime createdDate;
}
