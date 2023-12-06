package plate.back.domain.car.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import plate.back.domain.car.repository.CarRepository;
import plate.back.domain.predictedPlate.dto.PredictedPlateResponseDto;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService{

    private final CarRepository carRepo;

    @Override
    public boolean checkEnrollment(PredictedPlateResponseDto predRespDto) {

        return carRepo.findByLicensePlate(predRespDto.getModelPredictResult().getPredictedText()).isPresent();

    }
    
}
