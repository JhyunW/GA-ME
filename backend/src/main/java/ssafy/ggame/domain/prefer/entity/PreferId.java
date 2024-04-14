package ssafy.ggame.domain.prefer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.user.entity.User;

import java.io.Serializable;


@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PreferId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
}
