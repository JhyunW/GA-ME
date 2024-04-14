package ssafy.ggame.domain.userActionLog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.ggame.domain.userActionLog.dto.UserActionLogReqDto;
import ssafy.ggame.domain.userActionLog.dto.UserActionLogResDto;
import ssafy.ggame.domain.userActionLog.service.UserActionLogService;
import ssafy.ggame.global.common.BaseResponse;

import java.util.List;
import java.util.Map;

@Tag(name = "User Action Log Controller", description = "사용자 행동 패턴 API")
@RestController
@RequestMapping("/api/tracking")
public class UserActionLogController {

    private final UserActionLogService userActionLogService;

    @Autowired
    public UserActionLogController(UserActionLogService userActionLogService) {
        this.userActionLogService = userActionLogService;
    }

    // 스웨거에 schemes 항목에 표시하기 위해서는 컨트롤러의 리턴 타입에 명시를 해줘야 함
    @Operation(description = "사용자 행동 패턴을 카산드라 DB에 저장")
    @PostMapping("/log")
    public ResponseEntity<BaseResponse<UserActionLogResDto>> logUserAction(@RequestBody UserActionLogReqDto requestData) {
        UserActionLogResDto responseDto;

        responseDto = userActionLogService.loggingUserTagWeight(requestData.getUserId(), requestData.getPage(), requestData.getAction(), requestData.getArgs());

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(responseDto));
    }
}
