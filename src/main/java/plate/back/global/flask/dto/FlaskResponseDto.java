package plate.back.global.flask.dto;

import java.util.List;

import lombok.Getter;
import lombok.ToString;
import plate.back.domain.predictedPlate.entity.ModelPredictResult;

@ToString
@Getter
public class FlaskResponseDto {
    private int status;
    private String plateImg;
    private List<ModelPredictResult> predictedResults;
}
