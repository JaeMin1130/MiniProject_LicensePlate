package plate.back.domain.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import plate.back.domain.member.dto.MemberRequestDto;
import plate.back.domain.member.dto.MemberResponseDto;
import plate.back.domain.member.entity.Member;
import plate.back.domain.member.entity.Role;
import plate.back.domain.member.repository.MemberRepository;
import plate.back.domain.refreshToken.service.TokenService;
import plate.back.global.exception.CustomException;
import plate.back.global.exception.ErrorCode;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepo;
    private final TokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    // 1. 회원가입
    @Override
    public void signUp(MemberRequestDto.SignUp resqDto) {

        if (memberRepo.existsByMemberId(resqDto.getMemberId())) {
            throw new CustomException(ErrorCode.DUPLICATED_ID);
        }

        Member member = Member.builder()
                .memberId(resqDto.getMemberId())
                .password(passwordEncoder.encode(resqDto.getPassword()))
                .role(Role.MEMBER)
                .build();

        memberRepo.save(member);
    }

    // 2. 로그인
    @Override
    public MemberResponseDto signIn(MemberRequestDto.SignIn resqDto) {

        // 아이디 검증
        Optional<Member> option = memberRepo.findById(resqDto.getMemberId());
        if (!option.isPresent()) {
            throw new CustomException(ErrorCode.Member_NOT_FOUND);
        }
        
        log.info("아이디 검증 통과");

        // 비밀번호 검증
        Member member = option.get();
        String savedPassword = member.getPassword();
        String enteredPassword = resqDto.getPassword();

        log.info("입력한 비밀번호 : " + enteredPassword);
        log.info("저장된 비밀번호 : " + savedPassword);

        if (!passwordEncoder.matches(enteredPassword, savedPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        log.info("비밀번호 검증 통과");

        // accessToken, refreshToken 생성
        String accessToken = refreshTokenService.createAccessToken(member);
        String refreshToken = refreshTokenService.createRefreshToken(member);

        return MemberResponseDto.of(accessToken, refreshToken);
    }
}
