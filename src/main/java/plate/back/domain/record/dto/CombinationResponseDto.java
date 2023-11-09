package plate.back.domain.record.dto;

import java.util.ArrayList;

import lombok.Builder;
import plate.back.domain.image.dto.ImageResponseDto;
import plate.back.domain.predictedPlate.dto.PredictedPlateDto;

@Builder
public class CombinationResponseDto {

    private final RecordResponseDto record;
    private final ArrayList<PredictedPlateDto> predictedList;
    private final ImageResponseDto plateImage;

    public static CombinationResponseDto of(RecordResponseDto record, ArrayList<PredictedPlateDto> predictedList,
            ImageResponseDto plateImage) {

        return CombinationResponseDto.builder()
                .record(record)
                .predictedList(predictedList)
                .plateImage(plateImage)
                .build();
    }

}
