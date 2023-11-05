package plate.back.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.entity.Member;

public interface UserRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByMemberId(String email);

    boolean existsByMemberId(String email);
}