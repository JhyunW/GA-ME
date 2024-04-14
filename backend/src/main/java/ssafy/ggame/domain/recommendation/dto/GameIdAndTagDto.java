package ssafy.ggame.domain.recommendation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import ssafy.ggame.domain.tag.dto.TagDto;

import java.util.List;

@Getter
public class GameIdAndTagDto {
    @Schema(description = "게임 아이디", example = "123456")
    Long gameId;
    @Schema(description = "태그디티오(코드아이디, 태그아이디, 태그이름) 리스트")
    List<TagDto> tagList;
}
