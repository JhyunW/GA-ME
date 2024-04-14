package ssafy.ggame.domain.userActionLog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data // Lombok이 getter, setter, toString 등을 자동으로 생성
@Builder
public class UserActionLogResDto {
    @Schema(description = "사용자 아이디", example = "1")
    private Integer userId;
    @Schema(description = "행동을 감지한 시점", example = "2024-03-21 02:07:00.564")
    private LocalDateTime actionTime;
    @Schema(description = "행동을 시행한 페이지 이름", example = "detail")
    private String pageName;
    @Schema(description = "행동의 유형", example = "click")
    private String actionType;
    @Schema(description = "행동에 사용된 대상의 정보", example = "['{\"clicked_item\":\"youtube\"}', '{\"game_id\":\"1966720\"}']")
    private List<String> actionParams; // JSON 문자열로 저장된 파라미터
}
