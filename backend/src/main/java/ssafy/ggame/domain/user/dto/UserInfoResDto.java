package ssafy.ggame.domain.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoResDto {
    // 회원 아이디
    Integer userId;

    // 회원 이름
    String userName;

    // 회원 프로필 이미지
    String userProfileImg;

    // 신규 유저인지, 기존 유저인지
    Boolean isNewUser;
}
