package plate.back.domain.member.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import plate.back.global.response.ResponseDto;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository userRepo;
    private final ResponseDto response;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 1. 회원가입
    public ResponseEntity<?> signUp(MemberRequestDto.SignUp signUp) {
        if (userRepo.existsByMemberId(signUp.getMemberId())) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        Member user = Member.builder()
                .memberId(signUp.getMemberId())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .name(signUp.getName())
                .role(Authority.ROLE_MEMBER)
                .build();

        userRepo.save(user);

        return response.success("회원가입에 성공했습니다.");
    }

    // 2. 로그인
    public ResponseEntity<?> signIn(MemberRequestDto.SignIn signIn) {

        if (userRepo.findByMemberId(signIn.getMemberId().trim()).orElse(null) == null) {
            return response.fail("일치하는 아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = signIn.toAuthentication();

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername
        // 메서드가 실행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            return response.fail("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
    }
}
