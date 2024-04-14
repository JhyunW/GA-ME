package ssafy.ggame.domain.gameChoice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.ggame.domain.gameChoice.service.GameChoiceService;

@RestController
@RequestMapping("/api/game-choice")
@RequiredArgsConstructor
public class GameChoiceController {
    private final GameChoiceService gameChoiceService;

    @PostMapping("/{gameId}")
    public ResponseEntity<Object> saveGameChoice(@PathVariable Long gameId){
        return this.gameChoiceService.saveChoiceGame(gameId);
    }
}
