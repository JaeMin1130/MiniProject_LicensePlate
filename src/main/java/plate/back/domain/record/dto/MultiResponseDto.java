package plate.back.domain.record.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import plate.back.domain.image.dto.ImageResponseDto;
import plate.back.domain.predictedPlate.dto.PredictedPlateDto;

@Getter
@Builder
public class MultiResponseDto {

    private final RecordResponseDto record;
    private final List<PredictedPlateDto> predictedList;
    private final ImageResponseDto plateImage;

    public static MultiResponseDto of(RecordResponseDto record, List<PredictedPlateDto> predictedList,
            ImageResponseDto plateImage) {

        return MultiResponseDto.builder()
                .record(record)
                .predictedList(predictedList)
                .plateImage(plateImage)
                .build();
    }

}
