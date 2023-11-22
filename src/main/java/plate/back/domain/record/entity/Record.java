package plate.back.domain.record.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Record extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @Column(nullable = false, length = 20)
    private String modelType;

    @Column(nullable = false, length = 10)
    private String licensePlate;

    @Column(nullable = false)
    private Double accuracy;

    @Column(nullable = false, length = 10)
    private String state;

    @Builder
    public Record(String modelType, String licensePlate, Double accuracy, String state) {
        this.modelType = modelType;
        this.licensePlate = licensePlate;
        this.accuracy = accuracy;
        this.state = state;
    }

    public void updateLicensePlate(String licensePlate, String state) {
        this.licensePlate = licensePlate;
        this.state = state;
    }
}