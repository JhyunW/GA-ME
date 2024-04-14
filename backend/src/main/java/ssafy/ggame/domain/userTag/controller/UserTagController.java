package ssafy.ggame.domain.userTag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.ggame.domain.userActionLog.service.UserActionLogService;
import ssafy.ggame.domain.userTag.dto.UserTagDislikeReqDto;
import ssafy.ggame.domain.userTag.dto.UserTagResDto;
import ssafy.ggame.domain.userTag.service.UserTagService;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;

import java.util.List;

@Tag(name = "User Tag Controller", description = "사용자-태그 가중치 관계 API")
@RestController
@RequestMapping("/api/tracking")
public class UserTagController {
    @Autowired
    private UserTagService userTagService;
    private UserActionLogService userActionLogService;

    // 스웨거에 schemes 항목에 표시하기 위해서는 컨트롤러의 리턴 타입에 명시를 해줘야 함
    @Operation(description = "사용자 행동 패턴 기반 가중치 증가")
    // 게임에 대한 사용자 패턴 기반 가중치 업데이트
    @PutMapping()
    public ResponseEntity<BaseResponse<List<UserTagResDto>>> updateUserTagWeight(@RequestParam("userId") Integer userId,
                                                      @RequestParam("gameId") Long gameId,
                                                      @RequestParam("action") String action) {
        //가중치 업데이트
        List<UserTagResDto> resDto = userTagService.updateUserTagWeight(userId, gameId, action);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(resDto));
        // 성공적으로 처리되었음을 응답
    }

    @Operation(description = "사용자 행동 패턴 기반 가중치 감소(관심 없음)")
    // 관심 없음 행동 대상 태그 가중치 업데이트
    @PutMapping("/dislike")
    public ResponseEntity<BaseResponse<List<UserTagResDto>>> updateUserTagWeight(@RequestBody UserTagDislikeReqDto request) {
        List<UserTagResDto> resDto;

        // 가중치 업데이트
        resDto = userTagService.dislikeUserTagWeight(request.getUserId(), request.getTags());

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(resDto));
        // 성공적으로 처리되었음을 응답
    }
}
