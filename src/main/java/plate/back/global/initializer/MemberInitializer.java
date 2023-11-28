package plate.back.global.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import plate.back.domain.member.entity.Member;
import plate.back.domain.member.entity.Role;
import plate.back.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
@Component
public class MemberInitializer implements ApplicationRunner {

private final MemberRepository memberRepo;
private final PasswordEncoder passwordEncoder;

@Override
public void run(ApplicationArguments args) throws Exception {

memberRepo.save(Member.builder()
.memberId("admin")
.password(passwordEncoder.encode("123"))
.role(Role.ADMIN)
.build());
}
}
