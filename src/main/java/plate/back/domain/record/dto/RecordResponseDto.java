package plate.back.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import plate.back.domain.predictedPlate.entity.ModelType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordResponseDto {

    private Integer recordId;
    private ModelType modelType;
    private String licensePlate;
    private String accuracy;
    private String vehicleImage;
    private String plateImage;
    private String state;

    public void setState(String state) {
        this.state = state;
    }
}
