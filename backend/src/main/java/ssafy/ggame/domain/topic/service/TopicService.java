package ssafy.ggame.domain.topic.service;

import ssafy.ggame.domain.topic.dto.SaleGameDto;
import ssafy.ggame.domain.topic.dto.TopicNewsResDto;

import java.util.List;

public interface TopicService {
    List<TopicNewsResDto> hotTopic(Integer userId);
    List<SaleGameDto> salesInfo(Integer userId);
}
