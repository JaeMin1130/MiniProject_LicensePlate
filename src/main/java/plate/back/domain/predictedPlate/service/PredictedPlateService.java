package plate.back.domain.predictedPlate.service;

import plate.back.domain.predictedPlate.dto.PredictedPlateResponseDto;
import plate.back.domain.record.entity.Record;

public interface PredictedPlateService {
    
    public void savePredictedPlate(Record record, PredictedPlateResponseDto predRespDto);

}
