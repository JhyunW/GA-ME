package ssafy.ggame.domain.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ssafy.ggame.domain.user.dto.UserInfoResDto;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.domain.user.repository.UserRepository;
import ssafy.ggame.domain.user.service.AuthService;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${spring.kakao.client-id}")
    private String clientId;

    @Value("${spring.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/kakao/login")
    public ResponseEntity<String> redirectToKakaoOAuth() {
        String kakaoOAuthUrl = "https://kauth.kakao.com/oauth/authorize?"
                + "client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
        return ResponseEntity.ok(kakaoOAuthUrl);
    }
    @PostMapping("/kakao/callback")
    public ResponseEntity<BaseResponse<UserInfoResDto>> handleKakaoCallback(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken"); // 클라이언트로부터 받은 액세스 토큰

        // 카카오 API로부터 사용자 정보 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // 액세스 토큰 설정
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> userInfo = response.getBody();

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        System.out.println("email: "+email);

        Optional<User> userOptional = userRepository.findByUserEmail(email);

        boolean isNewUser = !userOptional.isPresent();
        User user = userOptional.orElseGet(() -> {
            User newUser = User.builder()
                    .userEmail(email)
                    .userName((String)((Map<String, Object>)userInfo.get("properties")).get("nickname"))
                    .userProfileImg((String)((Map<String, Object>)kakaoAccount.get("profile")).get("thumbnail_image_url"))
                    .userLastLoginDt(LocalDate.now())
                    .build();
            return userRepository.save(newUser);
        });

        UserInfoResDto userInfoResDto = authService.getUserInfo(user.getUserEmail());
        // 가져온 사용자 정보에 isNewUser 값 추가
        userInfoResDto.setIsNewUser(isNewUser); // UserInfoResDto에 isNewUser 값을 설정

        return ResponseEntity.ok(new BaseResponse<>(userInfoResDto));
    }
}
