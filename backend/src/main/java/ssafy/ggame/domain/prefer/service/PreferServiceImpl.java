package ssafy.ggame.domain.prefer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.game.repository.GameCustomRepository;
import ssafy.ggame.domain.game.repository.GameRepository;
import ssafy.ggame.domain.prefer.dto.PreferRequestDto;
import ssafy.ggame.domain.prefer.entity.Prefer;
import ssafy.ggame.domain.prefer.entity.PreferId;
import ssafy.ggame.domain.prefer.repository.PreferRepository;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.domain.user.repository.UserRepository;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PreferServiceImpl implements PreferService{
    private final PreferRepository preferRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameCustomRepository gameCustomRepository;


//    @Override
    public boolean savePrefer(PreferRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new BaseException(StatusCode.USER_NOT_FOUND));
        Game game = gameRepository.findById(requestDto.getGameId()).orElseThrow(() -> new BaseException(StatusCode.GAME_NOT_FOUND));
        Prefer prefer = Prefer.builder().preferId(PreferId.builder().user(user).game(game).build()).build();
        preferRepository.save(prefer);
        return true;
    }

    @Override
    public boolean deletePrefer(PreferRequestDto requestDto) {
        //1. 선호 삭제
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new BaseException(StatusCode.USER_NOT_FOUND));
        Game game = gameRepository.findById(requestDto.getGameId()).orElseThrow(() -> new BaseException(StatusCode.GAME_NOT_FOUND));

        Prefer result = preferRepository.findByPreferIdUserAndPreferIdGame(user, game).orElseThrow(()->new BaseException(StatusCode.PREFER_NOT_FOUND));
        preferRepository.delete(result);
        // 여기까지 성공적 처리시 true
        return true;
    }

    @Override
    public List<GameCardDto> getPreferList(Integer userId) {
        return gameCustomRepository.getPreferList(userId);
    }
}
