package plate.back.domain.predictedPlate.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import plate.back.domain.predictedPlate.dto.PredictedPlateResponseDto;
import plate.back.domain.predictedPlate.entity.PredictedPlate;
import plate.back.domain.predictedPlate.repository.PredictedPlateRepository;
import plate.back.domain.record.entity.Record;

@RequiredArgsConstructor
@Service
public class PredictedPlateServiceImpl implements PredictedPlateService{
    
    private final PredictedPlateRepository predRepo;

    @Override
    public void savePredictedPlate(Record record, PredictedPlateResponseDto predRespDto) {
    
        predRepo.save(PredictedPlate.builder()
                    .record(record)
                    .modelPredictResult(predRespDto.getModelPredictResult())
                    .isEnrolled(predRespDto.getIsEnrolled())
                    .build());

    }
    
}