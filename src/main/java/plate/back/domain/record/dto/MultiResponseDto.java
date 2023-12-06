package plate.back.domain.record.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import plate.back.domain.predictedPlate.dto.PredictedPlateResponseDto;

@Getter
@Builder
public class MultiResponseDto {

    private final RecordResponseDto record;
    private final List<PredictedPlateResponseDto> predictedPlateList;

    public static MultiResponseDto of(RecordResponseDto record, List<PredictedPlateResponseDto> predictedList) {

        return MultiResponseDto.builder()
                .record(record)
                .predictedPlateList(predictedList)
                .build();
    }

}
