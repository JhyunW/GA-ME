package ssafy.ggame.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.user.dto.UserInfoResDto;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.domain.user.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public UserInfoResDto getUserInfo(String email) {
        // UserRepository를 사용하여 이메일을 기반으로 사용자 정보를 조회합니다.
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 조회한 User 엔티티를 UserInfoResDto로 변환합니다.
        UserInfoResDto userInfoResDto = new UserInfoResDto();
        userInfoResDto.setUserId(user.getUserId());
        userInfoResDto.setUserName(user.getUserName());
        userInfoResDto.setUserProfileImg(user.getUserProfileImg());
        // isNewUser 등 추가적인 로직이 필요한 경우 여기에 구현합니다.

        return userInfoResDto;
    }
}
