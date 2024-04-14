package ssafy.ggame.domain.recommendation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.recommendation.dto.GameIdAndTagDto;
import ssafy.ggame.domain.recommendation.dto.RecommendationResponseDto;
import ssafy.ggame.domain.recommendation.dto.SearchGameRequestDto;
import ssafy.ggame.domain.recommendation.service.RecommendationService;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendation Controller", description = "Recommendation과 관련된 API")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/popular")
    @Operation(description = "인기 게임 추천")
    ResponseEntity<BaseResponse<List<GameCardDto>>> getPopularGameList(
            @Parameter(name = "userId", description = "유저 아이디, 로그인하지 않으면 0으로 줘 야함", example = "1")
            @RequestParam(required=true, defaultValue = "0") Integer userId,
            @Parameter(name = "codeId", description = "codeId(CAT/GEN), 전체라면 0으로 줘야함", example = "CAT")
            @RequestParam(required =true, defaultValue = "0") String codeId,
            @Parameter(name = "tagId", description = "tagId, 전체라면 0으로 줘야함", example = "11")
            @RequestParam(required = true, defaultValue = "0") Short tagId,
            @Parameter(name = "page", description = "페이지네이션, 이 값을 넘기지 않으면 자동으로 0이 들어감", example = "1")
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(name = "size", description = "size, 한번 요청시 가져올 데이터 개수, 안쓰면 100개씩 가져옴", example = "100")
            @RequestParam(name="size", defaultValue = "100") int size){

        List<GameCardDto> resultList = recommendationService.getPopularList(userId, codeId, tagId, page, size);
        System.out.println("resultList.size() = " + resultList.size());

        return ResponseEntity.ok(new BaseResponse<List<GameCardDto>>(resultList));
    }

    @GetMapping("/personal/{userId}")
    @Operation(description = "사용자 가중치 기반 인기 게임 추천")
    ResponseEntity<BaseResponse<RecommendationResponseDto>> getPersonalGameList(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") String codeId,
            @RequestParam(defaultValue = "0") Short tagId
    ){
        RecommendationResponseDto resultList = recommendationService.getPersonalList(userId, codeId, tagId);

        return ResponseEntity.ok(new BaseResponse<RecommendationResponseDto>(resultList));
    }


    @PostMapping("/search")
    @Operation(description = "게임 검색 추천")
    ResponseEntity<BaseResponse<RecommendationResponseDto>> searchGameList(@RequestBody SearchGameRequestDto searchGameRequestDto){
       RecommendationResponseDto resultList = recommendationService.searchGameList(searchGameRequestDto);

        return ResponseEntity.ok(new BaseResponse<RecommendationResponseDto>(resultList));

    }

    @GetMapping("/recent-popular")
    @Operation(description = "메인 배너에 걸릴 최신 인기 게임 10개")
    ResponseEntity<BaseResponse<List<GameCardDto>>> getRecentPopularGameList(){
        List<GameCardDto> resultList = recommendationService.getRecentPopularGameList();

        return ResponseEntity.ok(new BaseResponse<List<GameCardDto>>(resultList));
    }



}
