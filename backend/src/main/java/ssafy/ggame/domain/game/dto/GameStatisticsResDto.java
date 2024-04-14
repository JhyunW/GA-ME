package ssafy.ggame.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameStatisticsResDto {
    private String gameWordCloudUrl;
    private StatisticsDto statisticsDto;


    // 생성자 오버로딩: statisticsDto가 null일 때 처리
    public GameStatisticsResDto(String gameWordCloudUrl) {
        this.gameWordCloudUrl = gameWordCloudUrl;
        this.statisticsDto = null; // 또는 다른 방법으로 처리
    }

}
