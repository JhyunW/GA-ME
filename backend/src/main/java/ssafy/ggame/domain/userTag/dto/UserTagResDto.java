package ssafy.ggame.domain.userTag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTagResDto {
    @Schema(description = "사용자 아이디", example = "1")
    private Integer userId;
    @Schema(description = "태그 아이디", example = "123456")
    private Short tagId;
    @Schema(description = "코드 아이디", example = "A")
    private String codeId;
    @Schema(description = "태그 이름", example = "RPG")
    private String tagName;
    @Schema(description = "사용자-태그 가중치", example = "10")
    private Short userTagWeight;
}
