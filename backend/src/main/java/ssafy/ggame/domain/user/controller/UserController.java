package ssafy.ggame.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.ggame.domain.gameChoice.dto.GameChoiceDto;
import ssafy.ggame.domain.gameChoice.service.GameChoiceService;
import ssafy.ggame.domain.user.dto.UserDetailResDto;
import ssafy.ggame.domain.user.service.UserService;
import ssafy.ggame.global.common.BaseResponse;

import java.util.List;

@Tag(name = "User Controller", description = "회원과 관련된 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GameChoiceService gameChoiceService;

    // 스웨거에 schemes 항목에 표시하기 위해서는 컨트롤러의 리턴 타입에 명시를 해줘야 함
    @Operation(description = "회원의 정보, 좋아요 리스트, 선호태그 출력 ")
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<UserDetailResDto>> myPage(@PathVariable Integer userId){
        return this.userService.userDetail(userId);
    }

    @GetMapping("/choice-game")
    public ResponseEntity<BaseResponse<List<GameChoiceDto>>> surveyGames(){
        return this.gameChoiceService.getGameList();
    }


}
