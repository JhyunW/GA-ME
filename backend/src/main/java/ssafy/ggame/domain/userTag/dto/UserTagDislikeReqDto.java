package ssafy.ggame.domain.userTag.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserTagDislikeReqDto {
    @Schema(description = "사용자 아이디", example = "1")
    private Integer userId;
    @Schema(description = "행동 시점 페이지 이름", example = "main")
    private String page;
    @Schema(description = "관심 없음 대상 태그 리스트", example = "[\n" +
            "    {\"codeId\": 'A', \"tagId\": 123456},\n" +
            "    {\"codeId\": 'B', \"tagId\": 123456}\n" +
            "  ]")
    private List<TagCodePair> tags;

    @Getter
    public static class TagCodePair {
        private String codeId;
        private Short tagId;
    }
}
