package plate.back.domain.member.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import plate.back.domain.member.dto.MemberRequestDto;
import plate.back.domain.member.dto.MemberResponseDto;
import plate.back.domain.member.entity.Authority;
import plate.back.domain.member.entity.Member;
import plate.back.domain.member.repository.MemberRepository;
import plate.back.global.exception.CustomException;
import plate.back.global.exception.ErrorCode;
import plate.back.global.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 1. 회원가입
    public void signUp(MemberRequestDto.SignUp signUp) {
        if (userRepo.existsByMemberId(signUp.getMemberId())) {
            throw new CustomException(ErrorCode.DUPLICATED_ID);
        }

        Member user = Member.builder()
                .memberId(signUp.getMemberId())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .name(signUp.getName())
                .role(Authority.ROLE_MEMBER)
                .build();

        userRepo.save(user);
    }

    // 2. 로그인
    public MemberResponseDto signIn(MemberRequestDto.SignIn signInDto) {

        if (!userRepo.existsByMemberId(signInDto.getMemberId().trim())) {
            throw new CustomException(ErrorCode.Member_NOT_FOUND);
        }
        /*
         * 1. Login ID/PW 를 기반으로 Authentication 객체 생성
         * 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
         */
        UsernamePasswordAuthenticationToken authenticationToken = signInDto.toAuthentication();

        /*
         * 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
         * authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername
         * 메서드가 실행됨
         */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        if (!authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return MemberResponseDto.of(accessToken);
    }
}
