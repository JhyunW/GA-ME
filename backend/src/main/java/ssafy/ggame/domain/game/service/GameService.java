package ssafy.ggame.domain.game.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.dto.GameDetailResDto;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.game.repository.GameRepository;
import ssafy.ggame.domain.gameTag.entity.GameTag;
import ssafy.ggame.domain.gameTag.repository.GameTagRepository;
import ssafy.ggame.domain.prefer.repository.PreferRepository;
import ssafy.ggame.domain.recommendation.service.RecommendationService;
import ssafy.ggame.domain.tag.dto.TagDto;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.domain.user.repository.UserRepository;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final GameTagRepository gameTagRepository;
    private final PreferRepository preferRepository;

    /**
     *
     * @param gameId
     * @return List<GameCardDto>
     *     - gameId
     *     - gagList
     */
    public List<GameCardDto> getGameIdAndTagList(Long gameId){

        List<GameCardDto> gameCardDtoList = new ArrayList<>();

        List<Game> gameList = gameRepository.findAllByGameId(gameId);

        for(Game game : gameList){
            // 한 게임의 태그 리스트를 dto로 변환한 태그리스트 생성
            List<TagDto> tagList = new ArrayList<>();
            for(GameTag gameTag : game.getGameTags()){
                tagList.add(gameTag.convertToTagDto());
            }
            //gameId, tagList만 있는 GameCardDto 생성
            GameCardDto gameCardDto = GameCardDto.builder()
                    .gameId(game.getGameId())
                    .build();
            gameCardDto.updateTagList(tagList);
            // 리스트에 넣기
            gameCardDtoList.add(gameCardDto);
        }

        return gameCardDtoList;

    }

    public GameDetailResDto convertToGameDetailResDto(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new BaseException(StatusCode.GAME_NOT_FOUND));

        List<Game> relatedGameList = getPopularGamesByGameId(game);

        List<GameCardDto> relatedGames = null;
        if (relatedGameList != null) {
            relatedGames = recommendationService.makeGameCardDtoList(relatedGameList);
        }
        boolean isLikedByUser = preferRepository.existsByPreferIdUserUserIdAndPreferIdGameGameId(userId, gameId);

        List<GameTag> gameTags = gameTagRepository.findByGame_GameId(gameId);

        // 각 게임 태그를 TagDto로 변환하여 리스트로 만듭니다.
        List<TagDto> tagDtoList = gameTags.stream()
                .map(GameTag::convertToTagDto)
                .toList();

        return GameDetailResDto.builder()
                .gameId(game.getGameId())
                .gameName(game.getGameName())
                .gameShortDescription(game.getGameShortDescription())
                .gameDetailedDescription(game.getGameDetailedDescription())
                .gameHeaderImg(game.getGameHeaderImg())
                .gameWebsite(game.getGameWebsite())
                .gameDeveloper(game.getGameDeveloper())
                .gamePublisher(game.getGamePublisher())
                .gamePriceInitial(game.getGamePriceInitial())
                .gamePriceFinal(game.getGamePriceFinal())
                .gameDiscountPercent(game.getGameDiscountPercent())
                .gameIsLike(isLikedByUser)
                .gameTagList(tagDtoList)
                .gameReleaseDate(game.getGameReleaseDate())
                .screenshotList(convertJsonToList(game.getGameScreenshotImg())) // null or 리스트로 담기
                .relatedGameList(relatedGames)
                .build();
    }

    private List<Game> getPopularGamesByGameId(Game detailGame) {

        List<User> users = userRepository.findAllUsersByGame(detailGame);


        // 사용자가 좋아하는 게임을 counting하기 위한 맵 생성
        Map<Game, Integer> gameCountMap = new HashMap<>();
        // 사용자가 없는 경우 null 반환
        if (users.isEmpty()) {
            return null;
        }

        // 각 사용자의 선호(prefer) 목록에서 좋아하는 게임을 추출하여 counting
        for (User user : users) {
            List<Game> preferredGames = gameRepository.findPreferredGamesByUser(user);
            for (Game game : preferredGames) {
                if (game != detailGame) {
                    gameCountMap.put(game, gameCountMap.getOrDefault(game, 0) + 1); // 해당 게임의 count를 증가시킴
                }
            }
        }
        // gameCountMap이 완전히 비어 있는 경우 처리
        if (gameCountMap.isEmpty()) {
            return null;
        }

        // 맵을 카운트를 기준으로 내림차순 정렬하여 게임 ID 리스트 생성
        List<Map.Entry<Game, Integer>> sortedEntries = new ArrayList<>(gameCountMap.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 정렬된 게임 ID 리스트 생성
        List<Game> sortedGames = new ArrayList<>();
        int count = 0; // 추가된 게임 수를 추적하기 위한 변수

        for (Map.Entry<Game, Integer> entry : sortedEntries) {
            sortedGames.add(entry.getKey());
            count++;

            if (count >= 5) {
                break; // 5개의 게임을 추가하면 반복문을 종료합니다.
            }
        }
        return sortedGames;

    }

    private List<Map<String, String>> convertJsonToList(String gameScreenshotImg) {
        if (gameScreenshotImg == null) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(gameScreenshotImg);
            JsonNode screenshotsNode = jsonNode.get("screenshots");
            if (screenshotsNode != null && screenshotsNode.isArray()) {
                List<Map<String, String>> screenshotList = new ArrayList<>();
                for (JsonNode screenshot : screenshotsNode) {
                    Map<String, String> screenshotMap = new HashMap<>();
                    screenshotMap.put("id", screenshot.get("id").asText());
                    screenshotMap.put("path_full", screenshot.get("path_full").asText());
                    screenshotMap.put("path_thumbnail", screenshot.get("path_thumbnail").asText());
                    screenshotList.add(screenshotMap);
                }
                return screenshotList;
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
