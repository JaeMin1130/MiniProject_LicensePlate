package plate.back.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.PredictLogEntity;

public interface PredictLogRepository extends JpaRepository<PredictLogEntity, Integer> {
}
