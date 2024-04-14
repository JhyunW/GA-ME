package ssafy.ggame.domain.game.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.dto.GameLikeDto;
import ssafy.ggame.domain.game.dto.GameSaleCardDto;
import ssafy.ggame.domain.game.dto.GameTagsDto;
import ssafy.ggame.domain.recommendation.dto.TempDto;
import ssafy.ggame.domain.search.dto.SearchLikeRequestDto;
import ssafy.ggame.domain.tag.dto.TagDto;

import java.util.*;
import java.util.stream.Collectors;

import static ssafy.ggame.domain.game.entity.QGame.game;
import static ssafy.ggame.domain.gameTag.entity.QGameTag.gameTag;
import static ssafy.ggame.domain.prefer.entity.QPrefer.prefer;
import static ssafy.ggame.domain.tag.entity.QTag.tag;


@Repository
@RequiredArgsConstructor
public class GameCustomRepositoryImpl implements GameCustomRepository {

    private final JPAQueryFactory queryFactory;

    //게임 명 검색
    @Override
    public List<GameCardDto> findByGameNameContaining(SearchLikeRequestDto dto) {
        /*
        1. 대소문자 구분 X 검색
        2. finalScore 기준 내림차순
        3. 결과 Dto로 다 변환해서 return
        */
        Integer userId = dto.getUserId();
        //엔티티로 추출
        List<GameCardDto> searchGames = queryFactory.select(
                        Projections.constructor(
                                GameCardDto.class,
                                game.gameId.as("gameId"),
                                game.gameName.as("gameName"),
                                game.gameHeaderImg.as("gameHeaderImg"),
                                game.gamePriceInitial.as("gamePriceInitial"),
                                game.gamePriceFinal.as("gamePriceFinal"),
                                game.gameDeveloper.as("gameDeveloper")
                        )
                ).from(game)
                .where(game.gameName.containsIgnoreCase(dto.getKeyword()))
                .orderBy(game.gameFinalScore.desc())
                .fetch();

        //해당하는 게임들 id를 추출
        List<Long> ids = searchGames.stream()
                .map(GameCardDto::getGameId) // Game 객체에서 id를 추출
                .toList();
        //prefer 가져오기
        List<Long> preferIds = getPrefers(userId, ids);
        //해당하는 게임들 태그 가져오기
        Map<Long, List<TagDto>> tagsMap = getTags(ids);
        //좋아요 수 가져오기
        Map<Long, Long> likes = getLikes(ids);

        //게임에 매칭( tag, prefer, likes )
        searchGames.forEach(game -> {
            game.updateTagList(tagsMap.get(game.getGameId()));
            game.updateIsPrefer(preferIds.contains(game.getGameId()));
            game.updateLike(likes.get(game.getGameId()) == null ? 0 : likes.get(game.getGameId()));
        });
        return searchGames;
    }

    // 할인 게임 검색 메소드 ( 할인율 ~10%, ~30%, ~50%, ~75% )
    @Override
    public Map<Integer, List<GameSaleCardDto>> findSaleGames(Integer userId) {
        //1. 일단 할인율이 있는거 다가져오기 (10~30)
        List<GameSaleCardDto> searchGames = queryFactory.select(
                        Projections.constructor(
                                GameSaleCardDto.class,
                                game.gameId.as("gameId"),
                                game.gameName.as("gameName"),
                                game.gameHeaderImg.as("gameHeaderImg"),
                                game.gamePriceInitial.as("gamePriceInitial"),
                                game.gamePriceFinal.as("gamePriceFinal"),
                                game.gameDeveloper.as("gameDeveloper"),
                                game.gameDiscountPercent.as("gameDiscountPercent")
                        )
                ).from(game)
                .where(game.gameDiscountPercent.goe(10).and(game.gameDiscountPercent.lt(30)))
                .orderBy(game.gameFinalScore.desc())
                .limit(30)
                .fetch();
        searchGames.addAll(queryFactory.select(
                        Projections.constructor(
                                GameSaleCardDto.class,
                                game.gameId.as("gameId"),
                                game.gameName.as("gameName"),
                                game.gameHeaderImg.as("gameHeaderImg"),
                                game.gamePriceInitial.as("gamePriceInitial"),
                                game.gamePriceFinal.as("gamePriceFinal"),
                                game.gameDeveloper.as("gameDeveloper"),
                                game.gameDiscountPercent.as("gameDiscountPercent")
                        )
                ).from(game)
                .where(game.gameDiscountPercent.goe(30).and(game.gameDiscountPercent.lt(50)))
                .orderBy(game.gameFinalScore.desc())
                .limit(30)
                .fetch());
        searchGames.addAll(queryFactory.select(
                        Projections.constructor(
                                GameSaleCardDto.class,
                                game.gameId.as("gameId"),
                                game.gameName.as("gameName"),
                                game.gameHeaderImg.as("gameHeaderImg"),
                                game.gamePriceInitial.as("gamePriceInitial"),
                                game.gamePriceFinal.as("gamePriceFinal"),
                                game.gameDeveloper.as("gameDeveloper"),
                                game.gameDiscountPercent.as("gameDiscountPercent")
                        )
                ).from(game)
                .where(game.gameDiscountPercent.goe(50).and(game.gameDiscountPercent.lt(75)))
                .orderBy(game.gameFinalScore.desc())
                .limit(30)
                .fetch());
        searchGames.addAll(queryFactory.select(
                        Projections.constructor(
                                GameSaleCardDto.class,
                                game.gameId.as("gameId"),
                                game.gameName.as("gameName"),
                                game.gameHeaderImg.as("gameHeaderImg"),
                                game.gamePriceInitial.as("gamePriceInitial"),
                                game.gamePriceFinal.as("gamePriceFinal"),
                                game.gameDeveloper.as("gameDeveloper"),
                                game.gameDiscountPercent.as("gameDiscountPercent")
                        )
                ).from(game)
                .where(game.gameDiscountPercent.goe(75))
                .orderBy(game.gameFinalScore.desc())
                .limit(30)
                .fetch());


        //2. 선호, 태그들 가져오기
        //해당하는 게임들 id를 추출
        List<Long> ids = searchGames.stream()
                .map(GameSaleCardDto::getGameId) // Game 객체에서 id를 추출
                .toList();

        //prefer 가져오기
        List<Long> prefers = getPrefers(userId, ids);
        //tags 가져오기
        Map<Long, List<TagDto>> tags = getTags(ids);
        //좋아요 가져오기
        Map<Long, Long> likes = getLikes(ids);


        //세일 퍼센트 세팅 10,30,50,75
        Map<Integer, List<GameSaleCardDto>> sales = new HashMap<>();

        salePercentSetting(sales);
        //할인율 ~10%, ~30%, ~50%, ~75%

        //다 돌면서 분기 처리
        searchGames.forEach(game -> {
            game.updateTagList(tags.get(game.getGameId()));
            game.updateIsPrefer(prefers.contains(game.getGameId()));
            game.updateLike(likes.get(game.getGameId()) == null ? 0 : likes.get(game.getGameId()));
            game.updatePrices();
            Byte percent = game.getGameDiscountPercent();
            if (percent >= 10 && percent < 30) {
                sales.get(10).add(game);
            } else if (percent >= 30 && percent < 50) {
                sales.get(30).add(game);
            } else if (percent >= 50 && percent < 75) {
                sales.get(50).add(game);
            } else if (percent >= 75) {
                sales.get(75).add(game);
            }
        });

        return sales;
    }

    private static void salePercentSetting(Map<Integer, List<GameSaleCardDto>> sales) {
        sales.put(10, new ArrayList<>());
        sales.put(30, new ArrayList<>());
        sales.put(50, new ArrayList<>());
        sales.put(75, new ArrayList<>());
    }


    @Override
    public List<GameCardDto> getPreferList(Integer userId) {
        //선호하는 게임들 추출
        List<GameCardDto> gameList = queryFactory.select(
                        Projections.constructor(
                                GameCardDto.class,
                                prefer.preferId.game.gameId.as("gameId"),
                                prefer.preferId.game.gameName.as("gameName"),
                                prefer.preferId.game.gameHeaderImg.as("gameHeaderImg"),
                                prefer.preferId.game.gamePriceInitial.as("gamePriceInitial"),
                                prefer.preferId.game.gamePriceFinal.as("gamePriceFinal"),
                                prefer.preferId.game.gameDeveloper.as("gameDeveloper")
                        )
                ).from(prefer)
                .leftJoin(game).on(prefer.preferId.game.gameId.eq(game.gameId))
                .where(prefer.preferId.user.userId.eq(userId))
                .fetch();
        List<Long> ids = gameList.stream()
                .map(GameCardDto::getGameId) // Game 객체에서 id를 추출
                .toList();
        //해당 게임 Tag들 가져옴.
        Map<Long, List<TagDto>> tagsMap = getTags(ids);
        // 좋아요 수 추출
        Map<Long, Long> likes = getLikes(ids);
        //돌면서 DTO값 세팅
        gameList.forEach(game -> {
            game.updateTagList(tagsMap.get(game.getGameId()));
            game.updateIsPrefer(true);
            game.updateLike(likes.get(game.getGameId()) == null ? 0 : likes.get(game.getGameId()));
        });
        return gameList;
    }

    @Override
    public Page<TempDto> findAllGameAndTagList(List<Long> gameIds, Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(
                        game.gameId,
                        game.gameFinalScore,
                        game.gameName,
                        game.gameHeaderImg,
                        game.gamePriceInitial,
                        game.gamePriceFinal,
                        game.gameDeveloper,
                        tag.tagId.code.codeId,
                        tag.tagId.tagId,
                        tag.tagName
                )
                .from(game)
                .leftJoin(game.gameTags, gameTag)
                .leftJoin(gameTag.tag, tag)
                .where(
                        game.gameId.in(gameIds),
                        game.gameName.lower().notLike("%dsl%"),
                        game.gameName.lower().notLike("%soundtrack%")
                )
                .orderBy(game.gameFinalScore.desc()) // gameFinalScore를 내림차순으로 정렬
                .fetch();

        Set<Long> existingGameId = new HashSet<>();
        List<TempDto> resultList = new ArrayList<>();
        Map<Long, List<TagDto>> gameTagMap = new HashMap<>();
        for (Tuple result : results) {
            Long gameId = result.get(game.gameId);
            if (!existingGameId.contains(gameId)) {
                TempDto tempDto = TempDto.builder()
                        .gameId(result.get(game.gameId))
                        .gameFinalScore(result.get(game.gameFinalScore))
                        .gameName(result.get(game.gameName))
                        .gameHeaderImg(result.get(game.gameHeaderImg))
                        .gamePriceInitial(result.get(game.gamePriceInitial))
                        .gamePriceFinal(result.get(game.gamePriceFinal))
                        .gameDeveloper(result.get(game.gameDeveloper))
                        .build();

                resultList.add(tempDto);
                existingGameId.add(gameId);
            }


            List<TagDto> tagDtos = gameTagMap.computeIfAbsent(gameId, id -> new ArrayList<>());

            TagDto tagDto = TagDto.builder()
                    .codeId(result.get(tag.tagId.code.codeId))
                    .tagId(result.get(tag.tagId.tagId))
                    .tagName(result.get(tag.tagName))
                    .build();

            tagDtos.add(tagDto);
        }

        for (TempDto tempDto : resultList) {
            tempDto.updateTagList(gameTagMap.get(tempDto.getGameId()));
        }

        // 결과를 페이지로 변환하여 반환
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());


        return new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());


//        return resultList;
    }

    @Override
    public List<TempDto> findAllGameAndTagList(List<Long> gameIds) {
        List<Tuple> results = queryFactory
                .select(
                        game.gameId,
                        game.gameFinalScore,
                        game.gameName,
                        game.gameHeaderImg,
                        game.gamePriceInitial,
                        game.gamePriceFinal,
                        game.gameDeveloper,
                        tag.tagId.code.codeId,
                        tag.tagId.tagId,
                        tag.tagName
                )
                .from(game)
                .leftJoin(game.gameTags, gameTag)
                .leftJoin(gameTag.tag, tag)
                .where(
                        game.gameId.in(gameIds),
                        game.gameName.lower().notLike("%dsl%"),
                        game.gameName.lower().notLike("%soundtrack%")
                )
                .fetch();

        List<TempDto> resultList = new ArrayList<>();
        Map<Long, List<TagDto>> gameTagMap = new HashMap<>();
        for (Tuple result : results) {

            TempDto tempDto = TempDto.builder()
                    .gameId(result.get(game.gameId))
                    .gameFinalScore(result.get(game.gameFinalScore))
                    .gameName(result.get(game.gameName))
                    .gameHeaderImg(result.get(game.gameHeaderImg))
                    .gamePriceInitial(result.get(game.gamePriceInitial))
                    .gamePriceFinal(result.get(game.gamePriceFinal))
                    .gameDeveloper(result.get(game.gameDeveloper))
                    .codeId(result.get(tag.tagId.code.codeId))
                    .tagId(result.get(tag.tagId.tagId))
                    .build();
            resultList.add(tempDto);


            Long gameId = result.get(game.gameId);
            List<TagDto> tagDtos = gameTagMap.computeIfAbsent(gameId, id -> new ArrayList<>());

            TagDto tagDto = TagDto.builder()
                    .codeId(result.get(tag.tagId.code.codeId))
                    .tagId(result.get(tag.tagId.tagId))
                    .tagName(result.get(tag.tagName))
                    .build();

            tagDtos.add(tagDto);
        }

        for (TempDto tempDto : resultList) {
            tempDto.updateTagList(gameTagMap.get(tempDto.getGameId()));
        }

        return resultList;

    }

    /*
        공통 메소드
        getPrefers : 유저의 선호게임 Id들만 가져온다.
        getTags : 해당하는 게임의 태그를 join해서 가져옴.
        getLikes : 좋아요 수
     */

    //User prefer Id List 가져오기
    private List<Long> getPrefers(Integer userId, List<Long> ids) {
        return queryFactory.select(
                        prefer.preferId.game.gameId
                ).from(prefer)
                .where(prefer.preferId.game.gameId.in(ids).and(prefer.preferId.user.userId.eq(userId)))
                .fetch();
    }
    // Map<게임ID,List<해당게임태그>> -> 게임 ID당 태그 묶어서 return
    private Map<Long, List<TagDto>> getTags(List<Long> ids) {
        //해당하는 게임들 태그 가져오기
        List<GameTagsDto> gameTags = queryFactory.select(
                        Projections.constructor(
                                GameTagsDto.class,
                                game.gameId.as("gameId"),
                                tag.tagId.code.codeId.as("codeId"),
                                tag.tagId.tagId.as("tagId"),
                                tag.tagName.as("tagName")
                        )
                ).from(gameTag)
                .join(gameTag.game, game)
                .join(gameTag.tag, tag)
                .where(gameTag.game.gameId.in(ids)).distinct().fetch();

        //게임Id를 기준으로 tags 묶어서 가져옴.
        return gameTags.stream()
                .collect(Collectors.groupingBy(GameTagsDto::getGameId,
                        Collectors.mapping(
                                gameTagsDto ->
                                        TagDto.builder()
                                                .codeId(gameTagsDto.getCodeId())
                                                .tagId(gameTagsDto.getTagId())
                                                .tagName(gameTagsDto.getTagName())
                                                .build()
                                , Collectors.toList())));

    }
    //좋아요 수 가져오기 (Map<게임ID,좋아요 수>)
    public Map<Long, Long> getLikes(List<Long> ids) {
        List<GameLikeDto> likes = queryFactory.select(
                        Projections.constructor(
                                GameLikeDto.class,
                                prefer.preferId.game.gameId.as("gameId"),
                                prefer.preferId.game.gameId.count().as("gameLike")
                        )
                ).from(prefer)
                .where(prefer.preferId.game.gameId.in(ids))
                .groupBy(prefer.preferId.game.gameId)
                .fetch();
        return likes.stream()
                .collect(Collectors.toMap(GameLikeDto::getGameId, GameLikeDto::getGameLike));
    }
}
