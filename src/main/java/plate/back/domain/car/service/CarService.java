package plate.back.domain.car.service;

import plate.back.domain.predictedPlate.dto.PredictedPlateResponseDto;

public interface CarService {
    
    public boolean checkEnrollment(PredictedPlateResponseDto predRespDto);

}
