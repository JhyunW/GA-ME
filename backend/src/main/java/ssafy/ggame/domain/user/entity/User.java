package ssafy.ggame.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ssafy.ggame.domain.prefer.entity.Prefer;
import ssafy.ggame.global.common.BaseCreatedTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name= "user")
public class User extends BaseCreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId; //사용자 아이디 (AI)

    @Column(name= "user_email", nullable = false, length = 50)
    @Email
    private String userEmail;   // 사용자 이메일

    @Column(name= "user_name", nullable = false, length = 30)
    private String userName;    // 사용자 이름

    @Column(name= "user_birth", nullable = true)
    private short userBirth; // 생년만 받아와서 계산하기
    
    @Setter //마지막 로그인 날짜만 수정
    @Column(name= "user_last_login_dt", nullable = false)
    private LocalDate userLastLoginDt; // 사용자의 마지막 로그인 날짜

    @Column(name= "user_profile_img", length = 500)
    private String userProfileImg;  // 사용자 프로필 이미지 URL

    @OneToMany(mappedBy = "preferId.user", fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    List<Prefer> prefers = new ArrayList<>();

    public void updateUserProfile(String name, String imageUrl){
        this.userName = name;
        this.userProfileImg = imageUrl;
    }
}
