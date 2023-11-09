package plate.back.domain.predictedPlate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.predictedPlate.entity.PredictedPlate;

public interface PredictedPlateRepository extends JpaRepository<PredictedPlate, Integer> {
}
