package ssafy.ggame.domain.topic.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TopicNewsResDto {
    private String hotTopicLink;
    private String hotTopicImg;
    private String hotTopicTitle;
    private String hotTopicShortDesc;
    private LocalDate hotTopicDate;
}
