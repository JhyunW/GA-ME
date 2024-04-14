package ssafy.ggame.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    private Long gameId;
    private LocalDate statisticsBaseAt;
    private List<Integer> timeValues; // 시간 값 리스트
    private List<Integer> positiveCounts; // 긍정 댓글 개수 리스트
    private List<Integer> negativeCounts; // 부정 댓글 개수 리스트
}