package plate.back.domain.history.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryResponseDto {
    private Integer id;
    private Integer recordId;
    private String memberId;
    private String workType;
    private String previousText;
    private String currentText;
    private LocalDateTime createdDate;
}