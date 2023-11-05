package plate.back.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.PredictPlate;

public interface PredictLogRepository extends JpaRepository<PredictPlate, Integer> {
}
