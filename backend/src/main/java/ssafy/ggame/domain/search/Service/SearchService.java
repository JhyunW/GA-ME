package ssafy.ggame.domain.search.Service;

import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.search.dto.SearchLikeRequestDto;

import java.util.List;

public interface SearchService {
    List<GameCardDto> searchLikeGame (SearchLikeRequestDto dto);
}
