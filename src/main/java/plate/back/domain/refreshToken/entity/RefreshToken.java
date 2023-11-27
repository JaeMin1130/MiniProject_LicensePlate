package plate.back.domain.refreshToken.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import plate.back.domain.member.entity.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {

    @Id
    private String id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    @Column(nullable = false)
    private String value;
    
    @Builder
    public RefreshToken(Member member, String value){
        this.member = member;
        this.value = value;
    }
    
}
