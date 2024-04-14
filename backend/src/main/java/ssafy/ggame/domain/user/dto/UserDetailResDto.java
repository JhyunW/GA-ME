package ssafy.ggame.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.userTag.dto.UserTagDto;

import java.util.List;
@Builder
@ToString
@Data
public class UserDetailResDto {
    @Schema(description = "사용자의 시퀀스", example = "1")
    @NotNull(message = "사용자의 시퀀스는 null이 될 수 없습니다 ")
    // 회원 아이디
    Integer userId;

    @Schema(description = "사용자의 이름", example = "박종국")
    // 회원 이름
    String userName;

    @Schema(description = "사용자의 프로필 이미지", example = "https://lh3.googleusercontent.com/a/ACg8ocIt1qYl_1Zmsi7Ypfv7GbteOd3Y42Gktt-GCyp1Mqw1=s96-c")
    // 회원 프로필 이미지
    String userProfileImg;

    @Schema(description = "사용자의 만 나이", example = "25")
    // 회원 만나이 표시
    Integer userAge;

    @Schema(description = "사용자가 좋아요한 게임의 정보")
    // 선호게임 출력을 위한 리스트
    List<GameCardDto> preferList;

    @Schema(description = "사용자의 선호 태그 1등부터 10등까지의 리스트")
    // 선호 태그와 나의 취향 분석 그래프 표시를 위한 리스트
    List<UserTagDto> tagWeightList;
}
