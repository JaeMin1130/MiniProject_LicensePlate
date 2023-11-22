package plate.back.domain.predictedPlate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ModelPredictResult {

    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    private String predictedText;

    @Column(columnDefinition = "decimal(4, 2)") // 정수 2, 소수 2
    private Double accuracy;

}
