package ssafy.ggame.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ggame.domain.tag.dto.TagDto;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDetailResDto {
    // 게임 아이디
    private Long gameId;

    // 게임 이름
    private String gameName;

    // 게임 간단 설명
    private String gameShortDescription;

    // 게임 설명
    private String gameDetailedDescription;

    // 헤더 이미지
    private String gameHeaderImg;

    // 웹사이트
    private String gameWebsite;

    // 개발자
    private String gameDeveloper;

    // 배급사
    private String gamePublisher;

    // 게임 원가
    private Integer gamePriceInitial;

    // 게임 할인가
    private Integer gamePriceFinal;

    // 게임 할인율
    private Byte gameDiscountPercent;

    // 게임 출시 년월일
    private String gameReleaseDate;

    // 좋아요 여부
    private Boolean gameIsLike;

    // 태그 리스트
    private List<TagDto> gameTagList;

    // 스크린샷 이미지
    private List<Map<String, String>> screenshotList;

    // 관련 게임 리스트
    private List<GameCardDto> relatedGameList;

    // 생성자, Getter 및 Setter 생략
}
