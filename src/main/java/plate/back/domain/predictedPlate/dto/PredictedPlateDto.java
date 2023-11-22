package plate.back.domain.predictedPlate.dto;

import lombok.Builder;
import lombok.Getter;
import plate.back.domain.predictedPlate.entity.Enrollment;
import plate.back.domain.predictedPlate.entity.ModelPredictResult;

@Getter
@Builder
public class PredictedPlateDto {

    private ModelPredictResult modelPredictResult;
    private Enrollment isEnrolled;

    public static PredictedPlateDto convertIntoDto(ModelPredictResult modelPredictResult) {
        return PredictedPlateDto.builder().modelPredictResult(modelPredictResult).build();
    }

    public void setIsEnrolled(Enrollment isEnrolled) {
        this.isEnrolled = isEnrolled;
    }
}
