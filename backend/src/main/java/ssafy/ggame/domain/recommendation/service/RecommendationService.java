package ssafy.ggame.domain.recommendation.service;

import com.querydsl.core.Tuple;
import jdk.javadoc.doclet.Taglet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.code.entity.Code;
import ssafy.ggame.domain.code.repository.CodeRepository;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.game.repository.GameCustomRepository;
import ssafy.ggame.domain.game.repository.GameRepository;
import ssafy.ggame.domain.gameTag.entity.GameTag;
import ssafy.ggame.domain.gameTag.repository.GameTagRepository;
import ssafy.ggame.domain.prefer.entity.Prefer;
import ssafy.ggame.domain.prefer.repository.PreferRepository;
import ssafy.ggame.domain.recommendation.dto.GameIdAndTagDto;
import ssafy.ggame.domain.recommendation.dto.RecommendationResponseDto;
import ssafy.ggame.domain.recommendation.dto.SearchGameRequestDto;
import ssafy.ggame.domain.recommendation.dto.TempDto;
import ssafy.ggame.domain.tag.dto.TagDto;
import ssafy.ggame.domain.tag.entity.Tag;
import ssafy.ggame.domain.tag.repository.TagRepository;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.domain.user.repository.UserRepository;
import ssafy.ggame.domain.userTag.dto.UserTagDto;
import ssafy.ggame.domain.userTag.entity.UserTag;
import ssafy.ggame.domain.userTag.repository.UserTagCustomRepository;
import ssafy.ggame.domain.userTag.repository.UserTagRepository;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final TagRepository tagRepository;
    private final GameRepository gameRepository;
    private final GameTagRepository gameTagRepository;
    private final PreferRepository preferRepository;
    private final UserTagRepository userTagRepository;
    private final GameCustomRepository gameCustomRepository;
    private final UserTagCustomRepository userTagCustomRepository;

    /**
     * 전체 인기 게임 추천
     *
     * @param userId
     * @param codeId
     * @param tagId
     * @param page
     * @param size
     * @return
     */
    public List<GameCardDto> getPopularList(Integer userId, String codeId, Short tagId, int page, int size) {
        List<GameCardDto> gameCardDtoList = null;
        // 전체 게임 인기 순위
        // 로그인 전
        if (userId == 0) {
            // codeId, tagId에 따라 인기게임 가져오기
            gameCardDtoList = getGameCardList(codeId, tagId, page, size);
        }
        // 로그인 후
        else {
            // 사용자가 존재하지 않을 때 예외 발생
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(StatusCode.USER_NOT_FOUND));

            // codeId, tagId에 따라 인기게임 가져오기
            gameCardDtoList = getGameCardList(codeId, tagId, page, size);

            // userId에 따라  isPrefer 가져와 업데이트 하기
            // - 유저가 선호하는 게임 아이디 목록 가져오기
            List<Prefer> preferList = preferRepository.findByUserId(userId);
            HashSet<Long> preferGameIdSet = new HashSet<>();
            for (Prefer prefer : preferList) {
                preferGameIdSet.add(prefer.getPreferId().getGame().getGameId());
            }

            // - 인기게임에 선호하는 게임이 포함되어 있으면 인기게임의 isprefer를 true로 지정
            for (GameCardDto gameCardDto : gameCardDtoList) {
                if (preferGameIdSet.contains(gameCardDto.getGameId())) {
                    gameCardDto.updateIsPrefer(true);
                }
            }
        }
        return gameCardDtoList;
    }

    /**
     * 사용자 맞춤 게임 추천
     *
     * @param userId
     * @return
     */
    public RecommendationResponseDto getPersonalList(Integer userId, String codeId, Short tagId) {

        // 사용자 존재 유무 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(StatusCode.USER_NOT_FOUND));

        // 1. 사용자 가중치 전부 가져오기
        List<UserTagDto> userTagList = userTagCustomRepository.findByUserId(user.getUserId());

        // 2. 가져온 태그 - 사용자 가중치 맵으로 만들기 (TagDto - weight)
        Map<TagDto, Long> tagWeightMap = new HashMap<>();
        for (UserTagDto userTag : userTagList) {
            TagDto tagDto = TagDto.builder()
                    .codeId(userTag.getCodeId())
                    .tagId(userTag.getTagId())
                    .tagName(userTag.getTagName())
                    .build();
            tagWeightMap.put(tagDto, Long.valueOf(userTag.getUserTagWeight()));

        }


        // 3. 입력으로 받은 태그를 빈도수 별로 정렬해서 결과로 반환
        // - tagWeightMap을 value 내림차순으로 정렬
        List<TagDto> tagDtoList = getSortedTagDtoList(tagWeightMap);


        // 5. 검색 결과에 보여줄 정해진 개수만큼 태그 반환 (9개) - 메인 필터링을 위해
        List<TagDto> resultTagDtoList = tagDtoList.stream()
                .limit(9) // 0부터 8번째 요소까지
                .toList();

        // 6. 게임별 점수를 저장할 맵 (게임 아이디 - 가중치 합)
//        Map<TempDto, Double> gameScoreMap = calculateScore(tagDtoList, tagWeightMap);
        Map<TempDto, Double> gameScoreMap = calculateScore(resultTagDtoList, tagWeightMap);

        // 점수계산을 마쳤으니 내림차순으로 정렬하고,
        // GameCardDto형식으로 변환해서
        // 개수 잘라 반환하기
        List<Map.Entry<TempDto, Double>> sortedGameScoreList = getSortedGameScoreList(gameScoreMap);

//        System.out.println("getSortedGameScoreList Done =========================== ");


        // 100개만 잘라서 가져오기
        List<Map.Entry<TempDto, Double>> subList = sortedGameScoreList.subList(0, 100);

        System.out.println("subList.size() = " + subList.size());


        List<GameCardDto> gameCardDtoList = new ArrayList<>();
        // TODO: tagId, codeId 둘다 0이면
        if (codeId.equals("0") && tagId == 0) {
            // 반환형식인 gameCardDto로 변환하기
            gameCardDtoList = sortedGameCardDtoList(userId, subList);

            System.out.println("gameCardDtoList.size() = " + gameCardDtoList.size());
        }

        // TODO: 둘다 0이 아니면
        else if (!codeId.equals("0") && tagId != 0) {
            List<Map.Entry<TempDto, Double>> filteredList = new ArrayList<>();
            // 걸러진 맟춤 100개의 게임 중에
            for (Map.Entry<TempDto, Double> entry : subList) {
                TempDto game = entry.getKey();
                for (TagDto tagDto : game.getTagList()) {
                    // 요청으로 온 codeId, tagId가 포함된 게임이라면 필터링 리스트에 추가
                    if (tagDto.getCodeId().equals(codeId) && tagDto.getTagId() == tagId) {
                        filteredList.add(entry);
                    }
                }
            }
            gameCardDtoList = sortedGameCardDtoList(userId, filteredList);
        }

        return RecommendationResponseDto.builder()
                .tagDtoList(resultTagDtoList)
                .gameCardDtoList(gameCardDtoList)
                .build();
    }

    /**
     * 게임 검색 추천
     *
     * @param searchGameRequestDto
     * @return
     */
    public RecommendationResponseDto searchGameList(SearchGameRequestDto searchGameRequestDto) {

        Pageable pageable = PageRequest.of(0, 100);


        // 1. 게임 아이디, 게임 태그 리스트
        List<GameIdAndTagDto> gameIdAndTagDtoList = searchGameRequestDto.getGameIdAndTagDtoList();

        // 2. 담은 게임의 태그별 빈도수 세기(가중치) (TagDTO - 빈도수)
        Map<TagDto, Long> tagCntMap = new HashMap<>();
        for (GameIdAndTagDto gameIdAndTagDto : gameIdAndTagDtoList) {
            for (TagDto tagDto : gameIdAndTagDto.getTagList()) {
                Tag tag = tagRepository.findByCodeIdAndTagId(tagDto.getCodeId(), tagDto.getTagId()).orElseThrow(() -> new BaseException(StatusCode.TAG_NOT_EXIST));
                tagCntMap.put(tag.convertToTagDto(), tagCntMap.getOrDefault(tagDto, 0L) + 1L);
            }
        }

        // 3. 입력으로 받은 태그를 빈도수 별로 정렬해서 결과로 반환
        // - tagCntMap을 value 내림차순으로 정렬
        List<TagDto> tagDtoList = getSortedTagDtoList(tagCntMap);

        // 5. 검색 결과에 보여줄 정해진 개수만큼 태그 반환 (5개)
        List<TagDto> resultTagDtoList = tagDtoList.stream()
                .limit(5) // 0부터 4번째 요소까지
                .collect(Collectors.toList());

        // 6. 게임별 빈도수 점수 (gameId - 빈도수 점수)
        Map<TempDto, Double> gameScoreMap = calculateScore(tagDtoList, tagCntMap);

        // 점수계산을 마쳤으니 내림차순으로 정렬하고,
        // GameCardDto형식으로 변환해서
        // 개수 잘라 반환하기
        List<Map.Entry<TempDto, Double>> sortedGameScoreList = getSortedGameScoreList(gameScoreMap);

        // 20개만 잘라서 가져오기
        List<Map.Entry<TempDto, Double>> largeSubList = sortedGameScoreList.subList(0, 20);

        // 겹치는 게임 삭제
        List<Map.Entry<TempDto, Double>> subList = new ArrayList<>();
        for (Map.Entry<TempDto, Double> entry : largeSubList) {
            TempDto game = entry.getKey();
            boolean isContain = false;
            for (GameIdAndTagDto gameIdAndTagDto : gameIdAndTagDtoList) {
                if (Objects.equals(gameIdAndTagDto.getGameId(), game.getGameId())) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                subList.add(entry);
            }
        }

        // 15개 반환
        subList = subList.subList(0, 15);
        System.out.println("subList = " + subList);

        // 반환형식인 gameCardDto로 변환하기
        Integer userId = searchGameRequestDto.getUserId();
        List<GameCardDto> gameCardDtoList = sortedGameCardDtoList(userId, subList);

        return RecommendationResponseDto.builder()
                .tagDtoList(resultTagDtoList)
                .gameCardDtoList(gameCardDtoList)
                .build();

    }


    /**
     * 최신 인기 게임 추천 for banner
     *
     * @return
     */
    public List<GameCardDto> getRecentPopularGameList() {
        List<Game> recentTop10 = gameRepository.findFirst10ByOrderByGameFinalRecentScoreDesc();
        List<GameCardDto> gameCardDtoList = new ArrayList<>();


        // 받은 게임 별 총 좋아요 수 맵
        List<Long> ids = new ArrayList<>();
        recentTop10.forEach((g) -> ids.add(g.getGameId()));
        Map<Long, Long> likesMap = gameCustomRepository.getLikes(ids);
        for (Game game : recentTop10) {
            GameCardDto gameCardDto = game.converToGameCardDto();
            gameCardDto.updateLike(likesMap.getOrDefault(game.getGameId(), 0L));
            gameCardDtoList.add(gameCardDto);
        }
        return gameCardDtoList;
    }


    /**
     * codeId, gameId를 가진 게임을 게임 카드 디티오로 전환하여 반환하는 함수
     *
     * @param codeId
     * @param tagId
     * @param page
     * @param size
     * @return
     */
    private List<GameCardDto> getGameCardList(String codeId, Short tagId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<GameCardDto> gameCardDtoList = new ArrayList<>();
        // codeId, tagId에 따른 gameCardList 만들기
        // codeId 가 없을 때,
        Optional<Code> optionalCode = codeRepository.findByCodeId(codeId);
        if (!codeId.equals("0") && optionalCode.isEmpty()) {
            // 해당 코드가 존재하지 않는다는 예외 발생
            throw new BaseException(StatusCode.CODE_NOT_EXIST);
        }
        // tagId가 없을 때,
        Optional<Tag> optionalTag = tagRepository.findByCodeIdAndTagId(codeId, tagId);
        if (!codeId.equals("0") && optionalTag.isEmpty()) {
            // 해당 태그가 존재하지 않는다는 예외 발생
            throw new BaseException(StatusCode.TAG_NOT_EXIST);
        }

        // codeId, tagId가 둘 다 0일떄
        if (codeId.equals("0") && tagId == 0) {
            List<Game> gameList = gameRepository.findAllByOrderByGameFinalScoreDesc(pageable);
            gameCardDtoList = makeGameCardDtoList(gameList);
        }

        // codeId, tagId 둘 다 0이 아닐 때
        if (!codeId.equals("0") && tagId != 0) {
            // 모든 게임의 아이디 가져오기
            List<Long> gameIdList = new HashSet<>(gameTagRepository.findAllGameIdByCodeIdAndTagId(codeId, tagId)).stream().toList();
            // 게임카드디티오를 만들기위해 필요한 정보를 게임 아이디를 통해 가져오기(게임 가치점수로 정렬됨)
            Page<TempDto> gameTempDtoList = gameCustomRepository.findAllGameAndTagList(gameIdList, pageable);

            // 받은 게임 별 총 좋아요 수 맵
            List<Long> ids = new ArrayList<>();
            gameTempDtoList.forEach((g) -> ids.add(g.getGameId()));
            Map<Long, Long> likesMap = gameCustomRepository.getLikes(ids);

            gameCardDtoList = new ArrayList<>();
            //tempDto -> convertToGameCard
            for (TempDto tempDto : gameTempDtoList) {
                GameCardDto gameCardDto = tempDto.converToGameCardDto();
                gameCardDto.updateLike(likesMap.getOrDefault(tempDto.getGameId(), 0L));
                gameCardDtoList.add(gameCardDto);
            }

        }

        return gameCardDtoList;
    }


    /**
     * game리스트로 gameCardDto리스트 만들기
     *
     * @param gameList
     * @return
     */
    public List<GameCardDto> makeGameCardDtoList(List<Game> gameList) {
        List<GameCardDto> gameCardDtoList = new ArrayList<>();

        // 받은 게임 별 총 좋아요 수 맵
        List<Long> ids = new ArrayList<>();
        gameList.forEach((g) -> ids.add(g.getGameId()));
        Map<Long, Long> likesMap = gameCustomRepository.getLikes(ids);


        for (Game game : gameList) {
            GameCardDto gameCardDto = game.converToGameCardDto();
            //tagList 업데이트
            List<TagDto> tagDtoList = new ArrayList<>();
            for (GameTag tag : game.getGameTags()) {
                tagDtoList.add(tag.convertToTagDto());
            }
            gameCardDto.updateTagList(tagDtoList);
            gameCardDto.updateLike(likesMap.getOrDefault(game.getGameId(), 0L));
            gameCardDtoList.add(gameCardDto);
        }
        return gameCardDtoList;
    }

    private List<Map.Entry<TempDto, Double>> getSortedGameScoreList(Map<TempDto, Double> gameScoreMap) {
        // treeMap을 List로 변환
        List<Map.Entry<TempDto, Double>> sortedGameScoreList = new ArrayList<>(gameScoreMap.entrySet());

        // 점수(value)로 내림차순 정렬
        Collections.sort(sortedGameScoreList, valueComparator);
        return sortedGameScoreList;
    }


    /**
     * 추천을 위한 게임 점수 계산 함수
     *
     * @param tagDtoList
     * @param tagWeightMap
     * @return
     */
    private Map<TempDto, Double> calculateScore(List<TagDto> tagDtoList, Map<TagDto, Long> tagWeightMap) {
        // TODO: 모든 게임아이디를 가져와서, 각 태그 별로 점수를 매기고 카드 디티오로 매핑해서 반환하면 되자나!

        List<Long> allGameId = gameRepository.findAllGameId();

        // 6. 게임별 빈도수 점수 (gameId - 빈도수 점수)
        Map<TempDto, Double> gameScoreMap = new TreeMap<>();

        // 7. 게임 점수 계산을 위해 게임 정보를 담을 집합
        Set<TempDto> containGameList = new HashSet<>();

//        List<TempDto> gameList = gameCustomRepository.findAllGameAndTag();
        List<TempDto> gameList = gameCustomRepository.findAllGameAndTagList(allGameId);

        for (TempDto game : gameList) {
            for (TagDto tagDto : tagDtoList) {
                if (game.getCodeId() != null && game.getTagId() != null && game.getCodeId().equals(tagDto.getCodeId()) && game.getTagId() == tagDto.getTagId()) {
                    containGameList.add(game);
                    if (game.getCodeId().equals("GEN")) {
                        gameScoreMap.put(game, gameScoreMap.getOrDefault(game, 0.0) + tagWeightMap.get(tagDto));
                        System.out.println("game = " + game);
                    } else if (game.getCodeId().equals("CAT")) {
                        // 코드가 CAT이면 가중치 20%만 추가(적은 비중)
                        gameScoreMap.put(game, gameScoreMap.getOrDefault(game, 0.0) + tagWeightMap.get(tagDto) * 0.05);
                    }
                }

            }
        }

        // 점수계산
        for (TempDto game : containGameList) {
            Double score1 = (Math.log(gameScoreMap.get(game) + 1)) / (3 + Math.log(gameScoreMap.get(game) + 1)) * 100 * 0.8;
            Double score2 = game.getGameFinalScore() * 0.2;
            gameScoreMap.put(game, score1 + score2);
        }

        return gameScoreMap;
    }


    /**
     * (태그-가중치/빈도수) 맵을 value로 내림차순 정렬하고,
     * 정렬된 태그만 리스트로 반환하는 함수
     *
     * @param tagWeightMap
     * @return
     */
    private static List<TagDto> getSortedTagDtoList(Map<TagDto, Long> tagWeightMap) {
        // 3. tagWeighMap을 value 내림차순으로 정렬
        ArrayList<Map.Entry<TagDto, Long>> tagWeightList = new ArrayList<>(tagWeightMap.entrySet());
        tagWeightList.sort((e1, e2) -> {
            int compare = e2.getValue().compareTo(e1.getValue()); // 빈도수를 내림차순으로 정렬
            if (compare == 0) { // 빈도수가 같을 때는 tagId 값을 비교하여 오름차순으로 정렬
                return e1.getKey().getTagId().compareTo(e2.getKey().getTagId());
            }
            return compare;
        });

        // 4. List<TagDto> tagDtoList 지정
        List<TagDto> tagDtoList = new ArrayList<>();
        for (Map.Entry<TagDto, Long> tag : tagWeightList) {
            tagDtoList.add(tag.getKey());
        }
        return tagDtoList;
    }

    /**
     * 게임 정보로 정렬된 gameCardDto리스트 반환하는 함수
     *
     * @param userId
     * @param list
     * @return
     */

    private List<GameCardDto> sortedGameCardDtoList(Integer userId, List<Map.Entry<TempDto, Double>> list) {
        List<GameCardDto> gameCardDtoList = new ArrayList<>();

        // 받은 게임 별 총 좋아요 수 맵
        List<Long> ids = new ArrayList<>();
        list.forEach((e) -> ids.add(e.getKey().getGameId()));
        Map<Long, Long> likesMap = gameCustomRepository.getLikes(ids);


        for (Map.Entry<TempDto, Double> entry : list) {
            TempDto game = entry.getKey();
            GameCardDto gameCardDto = game.converToGameCardDto();
            gameCardDto.updateLike(likesMap.getOrDefault(game.getGameId(), 0L)); // 게임 총 좋아요 수 업데이트
            // 선호 여부 업데이트
            if (preferRepository.existsByPreferId_User_UserIdAndPreferId_Game_GameId(userId, game.getGameId())) {
                gameCardDto.updateIsPrefer(true);
            }
//            List<TagDto> tagDtoList = new ArrayList<>();
//            tagDtoList.add(TagDto.builder()
//                    .codeId(game.getCodeId())
//                    .tagId(game.getTagId())
//                    .tagName(game.getTagName())
//                    .build());

//            gameCardDto.updateTagList(tagDtoList);
            gameCardDtoList.add(gameCardDto);
        }
        return gameCardDtoList;
    }

    /**
     * 게임 점수를 내림차순을 정렬하는데 필요한 compatrator
     */

    private Comparator<Map.Entry<TempDto, Double>> valueComparator = (e1, e2) -> {
        return e2.getValue().compareTo(e1.getValue()); // 내림차순으로 정렬;
    };
}
