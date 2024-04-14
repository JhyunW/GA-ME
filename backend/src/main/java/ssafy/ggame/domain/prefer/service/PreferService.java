package ssafy.ggame.domain.prefer.service;

import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.prefer.dto.PreferRequestDto;

import java.util.List;

public interface PreferService {
    boolean savePrefer(PreferRequestDto requestDto);
    boolean deletePrefer(PreferRequestDto requestDto);
    List<GameCardDto> getPreferList(Integer userId);

}
