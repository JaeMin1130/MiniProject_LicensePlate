package plate.back.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    
    MEMBER("ROLE_MEMBER"), 
    ADMIN("ROLE_ADMIN");

    private String value; // SecurityContext 저장할 때 사용

    /*
        SecurityContext 저장할 때는 value 사용
        나머지(DB 저장, SecurityConfig 설정)는 enum 클래스명 사용
     */
}
