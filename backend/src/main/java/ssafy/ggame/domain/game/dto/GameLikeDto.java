package ssafy.ggame.domain.game.dto;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameLikeDto {
    Long gameId;
    Long gameLike;
}
