package ssafy.ggame.domain.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.ggame.domain.game.dto.GameDetailResDto;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.game.repository.GameRepository;
import ssafy.ggame.domain.game.service.GameService;
import ssafy.ggame.domain.statistics.dto.GameStatisticsResDto;
import ssafy.ggame.domain.statistics.dto.StatisticsDto;
import ssafy.ggame.domain.statistics.entity.Statistics;
import ssafy.ggame.domain.statistics.repository.StatisticsRepository;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/games") // "/api/games" 경로로 매핑
public class GameController {

    private final GameRepository gameRepository;
    private final StatisticsRepository statisticsRepository;
    private final GameService gameService;

    @GetMapping("/{game_id}/info/{user_id}")
    public ResponseEntity<Object> getGameInfo(@PathVariable("game_id") Long gameId, @PathVariable("user_id") Long userId) {

        GameDetailResDto responseDto = gameService.convertToGameDetailResDto(gameId, userId);

        return ResponseEntity.ok(new BaseResponse<>(responseDto));
    }

    @GetMapping("/{game_id}/statistics")
    public ResponseEntity<Object> getGameStatistics(@PathVariable("game_id") Long gameId) {
        // 게임 정보 조회
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        Game game = optionalGame.orElseThrow(() -> new BaseException(StatusCode.GAME_NOT_FOUND));

        String gameWordCloudUrl = optionalGame.map(Game::getGameWordCloudUrl).orElse(null);

        // 최신 통계 정보 조회
        Optional<Statistics> optionalStatistics = statisticsRepository.findFirstByStatisticsIdGameOrderByStatisticsIdStatisticsBaseDtDesc(game);
        // 통계 정보 DTO 생성
        StatisticsDto statisticsDto = null;
        if (optionalStatistics.isPresent()) {
            statisticsDto = optionalStatistics.map(statistics -> {
                if (statistics.getGameStandardPlaytime() < 10) {
                    return null;
                }
                return StatisticsDto.builder()
                        .gameId(statistics.getStatisticsId().getGame().getGameId())
                        .statisticsBaseAt(statistics.getStatisticsId().getStatisticsBaseDt())
                        .timeValues(getTimeValues(statistics))
                        .positiveCounts(getPositiveCounts(statistics))
                        .negativeCounts(getNegativeCounts(statistics))
                        .build();
            }).orElse(null);
        }
        // 게임 통계 응답 DTO 생성
        GameStatisticsResDto responseDto;
        if (statisticsDto != null) {
            responseDto = new GameStatisticsResDto(gameWordCloudUrl, statisticsDto);
        } else {
            responseDto = new GameStatisticsResDto(gameWordCloudUrl);
        }
        // 최소한 하나의 값이 존재하는지 확인하여 응답 반환
        if (gameWordCloudUrl != null || statisticsDto != null) {
            return ResponseEntity.ok(new BaseResponse<>(responseDto));
        } else {
            throw new BaseException(StatusCode.GAME_STATISTICS_NOT_FOUND);
        }
    }





    // 긍정 댓글 개수 리스트 생성 메서드
    private List<Integer> getPositiveCounts(Statistics statistics) {
        return Arrays.asList(
                statistics.getStatisticsLike0(),
                statistics.getStatisticsLike10(),
                statistics.getStatisticsLike20(),
                statistics.getStatisticsLike30(),
                statistics.getStatisticsLike40(),
                statistics.getStatisticsLike50(),
                statistics.getStatisticsLike60(),
                statistics.getStatisticsLike70(),
                statistics.getStatisticsLike80(),
                statistics.getStatisticsLike90()
        );
    }

    // 부정 댓글 개수 리스트 생성 메서드
    private List<Integer> getNegativeCounts(Statistics statistics) {
        return Arrays.asList(
                statistics.getStatisticsUnlike0(),
                statistics.getStatisticsUnlike10(),
                statistics.getStatisticsUnlike20(),
                statistics.getStatisticsUnlike30(),
                statistics.getStatisticsUnlike40(),
                statistics.getStatisticsUnlike50(),
                statistics.getStatisticsUnlike60(),
                statistics.getStatisticsUnlike70(),
                statistics.getStatisticsUnlike80(),
                statistics.getStatisticsUnlike90()
        );
    }

    // 시간 값 리스트 생성 메서드
    private List<Integer> getTimeValues(Statistics statistics) {
        List<Integer> timeValues = new ArrayList<>();
        int baseTime = statistics.getGameStandardPlaytime() / 5; // 50%에 해당하는 값 / 10
        for (int i = 0; i <= 9; i++) {
            timeValues.add(baseTime * i); // 10%부터 90%까지 추가
        }
        return timeValues;
    }

}