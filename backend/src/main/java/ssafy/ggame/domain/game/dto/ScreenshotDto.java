package ssafy.ggame.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenshotDto {
    // 스크린샷 아이디
    private Integer screenshotId;

    // 썸네일 경로
    private String pathThumbnail;

    // 전체 이미지 경로
    private String pathFull;

    // 생성자, Getter 및 Setter 생략
}
