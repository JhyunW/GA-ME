package ssafy.ggame.domain.gameChoice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.game.repository.GameRepository;
import ssafy.ggame.domain.gameChoice.dto.GameChoiceDto;
import ssafy.ggame.domain.gameChoice.entity.GameChoice;
import ssafy.ggame.domain.gameChoice.repository.GameChoiceRepository;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameChoiceService {
    private final GameChoiceRepository gameChoiceRepository;
    private final GameRepository gameRepository;

    // 취향조사할 고정된 게임 전부 조회
    public ResponseEntity<BaseResponse<List<GameChoiceDto>>> getGameList(){
        List<GameChoice> gameChoiceList = this.gameChoiceRepository.findAll();

        if(gameChoiceList.isEmpty()) throw new BaseException(StatusCode.GAME_NOT_FOUND);

        List<GameChoiceDto> gameChoiceDtoList = new ArrayList<>();
        for(GameChoice gameChoice: gameChoiceList){
            GameChoiceDto gameChoiceDto = GameChoiceDto.builder()
                    .gameId(gameChoice.getGameId())
                    .gameChoiceName(gameChoice.getGameChoiceName())
                    .gameHeaderImg(gameChoice.getGameHeaderImg())
                    .build();
            gameChoiceDtoList.add(gameChoiceDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(gameChoiceDtoList));
    }

    // gameId를 입력하면 해당 게임의 정보를 game_choice 테이블에 저장
    // 유저가 사용하는게 아니라 개발자가 게임을 추가하고 싶을때 편하게 추가하기 위해서 일단 만들어 둠!
    public ResponseEntity<Object> saveChoiceGame(Long gameId){
        Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new BaseException(StatusCode.GAME_NOT_FOUND));
        GameChoiceDto gameChoiceDto = GameChoiceDto.builder()
                .gameId(game.getGameId())
                .gameChoiceName(game.getGameName())
                .gameHeaderImg(game.getGameHeaderImg())
                .build();

        GameChoice gameChoice = GameChoice.builder()
                .gameId(gameChoiceDto.getGameId())
                .gameChoiceName(gameChoiceDto.getGameChoiceName())
                .gameHeaderImg(gameChoiceDto.getGameHeaderImg())
                .build();

        this.gameChoiceRepository.save(gameChoice);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.GAME_SAVE));
    }


}
