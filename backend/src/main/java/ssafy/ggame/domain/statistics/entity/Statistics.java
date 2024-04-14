package ssafy.ggame.domain.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ggame.global.common.BaseCreatedTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "statistics")
public class Statistics extends BaseCreatedTimeEntity {
    @EmbeddedId
    private StatisticsId statisticsId;

    @Column(name = "statistics_like_0")
    private Integer statisticsLike0;
    @Column(name = "statistics_like_10")
    private Integer statisticsLike10;
    @Column(name = "statistics_like_20")
    private Integer statisticsLike20;
    @Column(name = "statistics_like_30")
    private Integer statisticsLike30;
    @Column(name = "statistics_like_40")
    private Integer statisticsLike40;
    @Column(name = "statistics_like_50")
    private Integer statisticsLike50;
    @Column(name = "statistics_like_60")
    private Integer statisticsLike60;
    @Column(name = "statistics_like_70")
    private Integer statisticsLike70;
    @Column(name = "statistics_like_80")
    private Integer statisticsLike80;
    @Column(name = "statistics_like_90")
    private Integer statisticsLike90;


    @Column(name = "statistics_unlike_0")
    private Integer statisticsUnlike0;
    @Column(name = "statistics_unlike_10")
    private Integer statisticsUnlike10;
    @Column(name = "statistics_unlike_20")
    private Integer statisticsUnlike20;
    @Column(name = "statistics_unlike_30")
    private Integer statisticsUnlike30;
    @Column(name = "statistics_unlike_40")
    private Integer statisticsUnlike40;
    @Column(name = "statistics_unlike_50")
    private Integer statisticsUnlike50;
    @Column(name = "statistics_unlike_60")
    private Integer statisticsUnlike60;
    @Column(name = "statistics_unlike_70")
    private Integer statisticsUnlike70;
    @Column(name = "statistics_unlike_80")
    private Integer statisticsUnlike80;
    @Column(name = "statistics_unlike_90")
    private Integer statisticsUnlike90;

    @Column(name = "game_standard_playtime")
    private Integer gameStandardPlaytime;


}
