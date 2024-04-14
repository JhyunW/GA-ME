package ssafy.ggame.domain.prefer.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.global.common.BaseCreatedTimeEntity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "prefer")
public class Prefer extends BaseCreatedTimeEntity {
    @EmbeddedId
    private PreferId preferId;
    @Builder
    public Prefer(PreferId preferId) {
        this.preferId = preferId;
    }
}
