package ssafy.ggame.domain.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.statistics.entity.Statistics;
import ssafy.ggame.domain.statistics.entity.StatisticsId;

import java.util.Optional;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, StatisticsId> {

    // 게임 ID를 기준으로 최신 통계 정보를 조회하는 메서드
    Optional<Statistics> findFirstByStatisticsIdGameOrderByStatisticsIdStatisticsBaseDtDesc(Game game);

}