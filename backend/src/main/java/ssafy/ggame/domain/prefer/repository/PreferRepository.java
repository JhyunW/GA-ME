package ssafy.ggame.domain.prefer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ggame.domain.game.entity.Game;
import org.springframework.data.jpa.repository.Query;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.prefer.entity.Prefer;
import ssafy.ggame.domain.prefer.entity.PreferId;
import ssafy.ggame.domain.user.entity.User;

import java.util.Optional;

import java.util.List;

public interface PreferRepository extends JpaRepository<Prefer, PreferId> {

    // 사용자와 게임으로 선호게임 찾기
    Optional<Prefer> findByPreferIdUserAndPreferIdGame(User user, Game game);

    // 사용자와 게임으로 선호게임 찾기
    Boolean existsByPreferId_User_UserIdAndPreferId_Game_GameId(Integer userId, Long gameId);

    boolean existsByPreferIdUserUserIdAndPreferIdGameGameId(Long userId, Long gameId);

    @Query("SELECT p FROM Prefer p where p.preferId.user.userId = :userId")
    List<Prefer> findByUserId(Integer userId);

}
