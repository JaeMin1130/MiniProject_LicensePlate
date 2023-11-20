package plate.back.global.flask.dto;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class FlaskResponseDto {
    private int status;
    private String plateImgUrl;
    private String plateImgTitle;
    private List<ModelPredictResult> predictedResults;
}
