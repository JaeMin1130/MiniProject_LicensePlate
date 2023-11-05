package plate.back.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import plate.back.entity.Member;
import plate.back.enums.Authority;
import plate.back.persistence.UserRepository;

@RequiredArgsConstructor
@Component
public class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        userRepo.save(Member.builder()
                .memberId("mary")
                .password(passwordEncoder.encode("1234"))
                .name("Mary")
                .role(Authority.ROLE_ADMIN)
                .build());
    }
}
