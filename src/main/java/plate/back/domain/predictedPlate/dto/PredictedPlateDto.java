package plate.back.domain.predictedPlate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PredictedPlateDto {
    private Integer logId;
    private String modelType;
    private String predictedText;
    private double accuracy;
    private boolean isPresent;

    public PredictedPlateDto(String modelType, String predictedText, double accuracy) {
        this.modelType = modelType;
        this.predictedText = predictedText;
        this.accuracy = accuracy;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
}
