package plate.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import plate.back.dto.ResponseDto;
import plate.back.dto.user.UserRequestDto;
import plate.back.lib.Helper;
import plate.back.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final ResponseDto response;

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDto.SignUp signUp, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.signUp(signUp);
    }

    // 2. 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserRequestDto.SignIn signIn, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.signIn(signIn);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody UserRequestDto.Reissue reissue, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return userService.reissue(reissue);
    }

    // @PostMapping("/logout")
    // public ResponseEntity<?> logout(@RequestBody UserRequestDto.Logout logout,
    // Errors errors) {
    // // validation check
    // if (errors.hasErrors()) {
    // return response.invalidFields(Helper.refineErrors(errors));
    // }
    // return userService.logout(logout);
    // }

    @GetMapping("/authority")
    public ResponseEntity<?> authority() {
        log.info("ADD ROLE_ADMIN");
        return userService.authority();
    }
}