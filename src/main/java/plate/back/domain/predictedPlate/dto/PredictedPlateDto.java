package plate.back.domain.predictedPlate.dto;

import lombok.Builder;
import lombok.Getter;
import plate.back.global.flask.dto.ModelPredictResult;

@Getter
@Builder
public class PredictedPlateDto {

    private ModelPredictResult modelPredictResult;
    private boolean isPresent;

    public static PredictedPlateDto convertIntoDto(ModelPredictResult modelPredictResult) {
        return PredictedPlateDto.builder().modelPredictResult(modelPredictResult).build();
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }
}
