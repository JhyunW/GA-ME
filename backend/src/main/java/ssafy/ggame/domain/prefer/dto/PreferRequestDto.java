package ssafy.ggame.domain.prefer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferRequestDto {
    private Integer userId;
    private Long gameId;
}
