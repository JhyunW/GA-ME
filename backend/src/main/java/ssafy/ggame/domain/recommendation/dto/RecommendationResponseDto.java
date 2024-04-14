package ssafy.ggame.domain.recommendation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.tag.dto.TagDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDto {
    @Schema(description = "사용자의 관심 태그상위 9개")
    List<TagDto> tagDtoList;

    @Schema(description = "사용자 관심태그 가반 추천 게임 리스트")
    List<GameCardDto> gameCardDtoList;
}
