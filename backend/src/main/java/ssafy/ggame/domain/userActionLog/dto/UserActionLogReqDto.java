package ssafy.ggame.domain.userActionLog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data // Lombok이 getter, setter, toString 등을 자동으로 생성
@NoArgsConstructor
@AllArgsConstructor
public class UserActionLogReqDto {
    @Schema(description = "사용자 아이디", example = "1")
    private Integer userId;
    @Schema(description = "행동을 시행한 페이지 이름", example = "detail")
    private String page;
    @Schema(description = "행동의 유형", example = "click")
    private String action;
    @Schema(description = "행동에 사용된 대상의 정보", example = "['{\"clicked_item\":\"youtube\"}', '{\"game_id\":\"1966720\"}']")
    private List<Map<String, Object>> args;
}
