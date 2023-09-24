package plate.back.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.CarInfoEntity;

public interface CarInfoRepository extends JpaRepository<CarInfoEntity, Integer> {
    public Optional<CarInfoEntity> findByLicensePlate(String plate);
}
