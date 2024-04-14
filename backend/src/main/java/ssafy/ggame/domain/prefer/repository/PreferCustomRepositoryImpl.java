package ssafy.ggame.domain.prefer.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static ssafy.ggame.domain.game.entity.QGame.game;
import static ssafy.ggame.domain.prefer.entity.QPrefer.prefer;

@Repository
@RequiredArgsConstructor
public class PreferCustomRepositoryImpl implements PreferCustomRepository{
    private final JPAQueryFactory queryFactory;
    //선호 게임 이름 가져오기
    @Override
    public List<String> findPreferGameNames(Integer userId) {
            List<Tuple> list = queryFactory.select(
                            game.gameName,
                            game.gameFinalScore
                    )
                    .from(prefer)
                    .join(prefer.preferId.game, game)
                    .where(prefer.preferId.user.userId.eq(userId))
                    .orderBy(game.gameFinalScore.desc())
                    .distinct()
                    .limit(5)
                    .fetch();
        return list.stream()
                .map(tuple -> tuple.get(game.gameName)) // Tuple에서 gameName 값 추출
                .toList(); // 추출된 값을 List<String>으로 수집
    }
}
