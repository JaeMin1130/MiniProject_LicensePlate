package plate.back.domain.car.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.car.entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {
    public Optional<Car> findByLicensePlate(String plate);
}
