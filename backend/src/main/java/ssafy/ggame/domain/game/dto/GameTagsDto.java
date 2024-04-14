package ssafy.ggame.domain.game.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GameTagsDto {
    Long gameId;
    String codeId;
    Short tagId;
    String tagName;
}
