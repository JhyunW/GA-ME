package ssafy.ggame.domain.gameChoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.gameChoice.entity.GameChoice;

import java.util.List;

public interface GameChoiceRepository extends JpaRepository<GameChoice, Long> {
}
