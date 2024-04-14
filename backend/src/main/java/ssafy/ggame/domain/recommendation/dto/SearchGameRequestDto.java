package ssafy.ggame.domain.recommendation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchGameRequestDto {

    @Schema(description = "사용자 아이디" ,example = "1")
    Integer userId;
    @Schema(description = "게임아이디 태그 디티오(게임아이디, 태그 디티오(코드 아이디, 태그아이디, 태그 이름)) 리스트")
    List<GameIdAndTagDto> gameIdAndTagDtoList;
}
