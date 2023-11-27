package plate.back.domain.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.refreshToken.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{
    
    public Optional<RefreshToken> findByValue(String refreshToken);
}
