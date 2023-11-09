package plate.back.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordDto {
    private Integer recordId;
    private String modelType;
    private String licensePlate;
    private String accuracy;
    private String vehicleImage;
    private String plateImage;
    private String state;

    public void setState(String state) {
        this.state = state;
    }
}
