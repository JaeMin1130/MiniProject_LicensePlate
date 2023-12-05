package plate.back.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import plate.back.domain.member.dto.MemberRequestDto;
import plate.back.domain.member.dto.MemberResponseDto;
import plate.back.domain.member.service.MemberService;

@Tag(name = "Member API")
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @Operation(summary = "회원가입", description = "아이디 중복 여부 검사 후, 비밀번호 인코딩하여 Role.USER로 저장")
    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody MemberRequestDto.SignUp signUp) {
        memberService.signUp(signUp);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    // 2. 로그인
    @Operation(summary = "로그인", description = "아이디, 비밀번호 검증 후, Refresh Token과 Access Token 발급")
    @PostMapping("/sign-in")
    public ResponseEntity<MemberResponseDto> signIn(@RequestBody MemberRequestDto.SignIn signIn) {
        MemberResponseDto memberResponseDto = memberService.signIn(signIn);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }
}