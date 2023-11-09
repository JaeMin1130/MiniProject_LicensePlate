package plate.back.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import plate.back.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByMemberId(String email);

    boolean existsByMemberId(String email);
}