package plate.back.global.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.global.jwt.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

}
