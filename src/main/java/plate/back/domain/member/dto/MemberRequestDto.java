package plate.back.domain.member.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class MemberRequestDto {

    @Getter
    public static class SignUp {

        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        private String memberId;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        private String name;

    }

    @Getter
    public static class SignIn {
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        private String memberId;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(memberId, password);
        }
    }

}
