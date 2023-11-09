package plate.back.domain.predictedPlate.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import plate.back.domain.record.entity.Record;

@Getter
@Entity
public class PredictedPlate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "record_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Record record;

    @Column(nullable = false, length = 10)
    private String modelType;

    @Column(nullable = false, length = 10)
    private String predictedText;

    @Column(nullable = false)
    private Double accuracy;

    @Column(nullable = false)
    private boolean isPresent;

    @Builder
    public PredictedPlate(Record record, String modelType, String predictedText, Double accuracy, boolean isPresent) {
        this.record = record;
        this.modelType = modelType;
        this.predictedText = predictedText;
        this.accuracy = accuracy;
        this.isPresent = isPresent;
    }
}