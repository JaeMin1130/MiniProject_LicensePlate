package plate.back.global.flask.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ModelPredictResult {
    private String modelType;
    private String predictedText;
    private Double accuracy;
}
