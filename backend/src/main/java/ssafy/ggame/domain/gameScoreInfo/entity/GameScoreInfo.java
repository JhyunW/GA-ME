package ssafy.ggame.domain.gameScoreInfo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ggame.global.common.BaseUpdatedEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "game_score_info")
public class GameScoreInfo extends BaseUpdatedEntity {
    @Id
    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "game_review_cnt")
    private Integer gameReviewCnt;

    @Column(name = "game_review_like_cnt")
    private Integer gameReviewLikeCnt;

    @Column(name = "game_review_unlike_cnt")
    private Integer gameReviewUnlikeCnt;

    @Column(name = "game_review_is_use_cnt")
    private Integer gameReviewIsUseCnt;
}

