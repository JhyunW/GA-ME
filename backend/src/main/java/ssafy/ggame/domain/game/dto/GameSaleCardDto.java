package ssafy.ggame.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ggame.domain.tag.dto.TagDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameSaleCardDto {
    Long gameId;
    String gameName;
    String gameHeaderImg;
    Integer gamePriceInitial;
    Integer gamePriceFinal;
    String gameDeveloper;
    Byte gameDiscountPercent;
    Long gameLike;
    Boolean isPrefer;
    List<TagDto> tagList;

    public void updateIsPrefer(boolean isPrefer){
        this.isPrefer = isPrefer;
    }

    public void updateTagList(List<TagDto> tagList){
        this.tagList = tagList;
    }
    public void updateLike(Long like){this.gameLike = like;}
    //한화로 적용하기 위해서 설정된 값에서 /100을 해준다.
    public void updatePrices(){
        this.gamePriceInitial/=100;
        this.gamePriceFinal/=100;
    }

    @Builder
    public GameSaleCardDto(Long gameId, String gameName, String gameHeaderImg, Integer gamePriceInitial, Integer gamePriceFinal, String gameDeveloper, Byte gameDiscountPercent) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameHeaderImg = gameHeaderImg;
        this.gamePriceInitial = gamePriceInitial;
        this.gamePriceFinal = gamePriceFinal;
        this.gameDeveloper = gameDeveloper;
        this.gameDiscountPercent = gameDiscountPercent;
    }
}
