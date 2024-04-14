package ssafy.ggame.domain.topic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ssafy.ggame.domain.topic.dto.HotTopicRequestDto;
import ssafy.ggame.domain.topic.dto.SaleGameDto;
import ssafy.ggame.domain.topic.dto.TopicNewsResDto;
import ssafy.ggame.domain.topic.service.TopicService;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/topics")
public class TopicController {
    private final TopicService topicService;
    @PostMapping("/news")
    public ResponseEntity<Object> hotTopic(@RequestBody HotTopicRequestDto dto){
        List<TopicNewsResDto> topicNewsResDtos = topicService.hotTopic(dto.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(topicNewsResDtos));

    }
        @PostMapping("/discount")
    public ResponseEntity<Object> discountGame(@RequestBody HotTopicRequestDto dto){
            List<SaleGameDto> saleGameDtos = topicService.salesInfo(dto.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(saleGameDtos));

    }

}
