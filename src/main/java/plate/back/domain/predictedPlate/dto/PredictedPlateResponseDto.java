package plate.back.domain.predictedPlate.dto;

import lombok.Builder;
import lombok.Getter;
import plate.back.domain.predictedPlate.entity.Enrollment;
import plate.back.domain.predictedPlate.entity.ModelPredictResult;

@Getter
@Builder
public class PredictedPlateResponseDto {

    private ModelPredictResult modelPredictResult;
    private Enrollment isEnrolled;

    public static PredictedPlateResponseDto convertIntoDto(ModelPredictResult modelPredictResult) {
        return PredictedPlateResponseDto.builder().modelPredictResult(modelPredictResult).build();
    }

    public void setIsEnrolled(Enrollment isEnrolled) {
        this.isEnrolled = isEnrolled;
    }
}
