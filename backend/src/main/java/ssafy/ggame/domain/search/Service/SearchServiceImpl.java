package ssafy.ggame.domain.search.Service;

import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.game.repository.GameCustomRepository;
import ssafy.ggame.domain.game.repository.GameRepository;
import ssafy.ggame.domain.game.service.GameService;
import ssafy.ggame.domain.search.dto.SearchLikeRequestDto;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final GameCustomRepository gameRepository;
    @Override
    public List<GameCardDto> searchLikeGame(SearchLikeRequestDto dto) {
        return gameRepository.findByGameNameContaining(dto);
    }
}
