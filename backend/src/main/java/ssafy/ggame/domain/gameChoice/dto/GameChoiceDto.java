package ssafy.ggame.domain.gameChoice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GameChoiceDto {
    private Long gameId;
    private String gameChoiceName;
    private String gameHeaderImg;
}
