package plate.back.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import plate.back.domain.member.dto.MemberRequestDto;
import plate.back.domain.member.service.MemberService;
import plate.back.global.response.ResponseDto;
import plate.back.global.utils.Helper;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService userService;
    private final ResponseDto response;

    // 1. 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody MemberRequestDto.SignUp signUp, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.signUp(signUp);
    }

    // 2. 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody MemberRequestDto.SignIn signIn, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.signIn(signIn);
    }
}