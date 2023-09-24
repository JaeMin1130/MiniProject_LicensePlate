package plate.back.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryDto {
    private Integer id;
    private Integer logId;
    private String userId;
    private String workType;
    private String previousText;
    private String currentText;
    private Date date;
}
