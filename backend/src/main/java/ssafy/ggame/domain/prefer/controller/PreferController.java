package ssafy.ggame.domain.prefer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.prefer.dto.PreferRequestDto;
import ssafy.ggame.domain.prefer.service.PreferService;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;

import java.util.List;


@RestController
@RequestMapping("/api/game/prefer")
@RequiredArgsConstructor
public class PreferController {
    /*
        가중치 감소는 다른 URL에서 합니둥
     */
    private final PreferService preferService;
    /*
        선호도 저장
     */
    @PostMapping
    public ResponseEntity<Object> savePrefer(@RequestBody PreferRequestDto saveRequestDto) {
        boolean result = preferService.savePrefer(saveRequestDto);
        if(!result){
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(StatusCode.PREFER_CANNOT_SAVE));
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(StatusCode.SUCCESS));
        }
    }
    /*
        선호도 삭제
     */
    @DeleteMapping
    public ResponseEntity<Object> deletePrefer(@RequestBody PreferRequestDto unPreferTagsRequestDto){
        // List<Tag정보> return 필요
        preferService.deletePrefer(unPreferTagsRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(StatusCode.SUCCESS));
    }

    /*
        선호도 리스트 가져오기
     */
    @PostMapping("/list")
    public ResponseEntity<Object> getPrefers(@RequestBody PreferRequestDto saveRequestDto) {
        List<GameCardDto> preferList = preferService.getPreferList(saveRequestDto.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(preferList));
    }


}
