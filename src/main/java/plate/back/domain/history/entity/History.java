package plate.back.domain.history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import plate.back.global.utils.BaseTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class History extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    private Integer recordId;

    @Column(nullable = false, length = 20)
    private String memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @Column(nullable = false, length = 10)
    private String previousText;

    @Column(nullable = false, length = 10)
    private String currentText;

    @Builder
    public History(Integer recordId, String memberId, TaskType taskType, String previousText, String currentText) {
        this.recordId = recordId;
        this.memberId = memberId;
        this.taskType = taskType;
        this.previousText = previousText;
        this.currentText = currentText;
    }
}
