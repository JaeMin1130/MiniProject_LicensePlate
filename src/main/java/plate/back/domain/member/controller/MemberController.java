package plate.back.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import plate.back.domain.member.dto.MemberRequestDto;
import plate.back.domain.member.dto.MemberResponseDto;
import plate.back.domain.member.service.MemberService;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody MemberRequestDto.SignUp signUp) {
        memberService.signUp(signUp);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<MemberResponseDto> signIn(@RequestBody MemberRequestDto.SignIn signIn) {
        MemberResponseDto memberResponseDto = memberService.signIn(signIn);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }
}