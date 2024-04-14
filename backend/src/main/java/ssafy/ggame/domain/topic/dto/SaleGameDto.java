package ssafy.ggame.domain.topic.dto;

import lombok.*;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.dto.GameSaleCardDto;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleGameDto {
    Integer salePercent;
    List<GameSaleCardDto> cardDtoList;
}
