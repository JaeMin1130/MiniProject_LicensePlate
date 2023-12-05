package plate.back.domain.predictedPlate.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import plate.back.domain.record.entity.Record;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PredictedPlate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer predictId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "record_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Record record;

    @Embedded
    private ModelPredictResult modelPredictResult;

    @Column(nullable = false)
    private Enrollment isEnrolled;

    @Builder
    public PredictedPlate(Record record, ModelPredictResult modelPredictResult, Enrollment isEnrolled) {
        this.record = record;
        this.modelPredictResult = modelPredictResult;
        this.isEnrolled = isEnrolled;
    }
}