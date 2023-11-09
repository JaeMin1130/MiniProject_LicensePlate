package plate.back.domain.car.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.car.entity.CarInfo;

public interface CarInfoRepository extends JpaRepository<CarInfo, Integer> {
    public Optional<CarInfo> findByLicensePlate(String plate);
}
