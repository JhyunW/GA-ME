package ssafy.ggame.domain.gameTag.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Builder
@Getter
@Setter
public class GameTagDto {
    private Long gameTagId;
    private Long gameId;
    private Short tagId;
    private String codeId;
}
