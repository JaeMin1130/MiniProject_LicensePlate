package plate.back.domain.member.service;

import plate.back.domain.member.dto.MemberRequestDto;
import plate.back.domain.member.dto.MemberResponseDto;

public interface MemberService {

    // 1. 회원가입
    public void signUp(MemberRequestDto.SignUp resqDto);

    // 2. 로그인
    public MemberResponseDto signIn(MemberRequestDto.SignIn resqDto);
    
}
