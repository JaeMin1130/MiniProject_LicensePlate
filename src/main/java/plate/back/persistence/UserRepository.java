package plate.back.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserId(String email);

    boolean existsByUserId(String email);
}