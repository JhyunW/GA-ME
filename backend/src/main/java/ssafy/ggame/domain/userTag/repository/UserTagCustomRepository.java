package ssafy.ggame.domain.userTag.repository;

import ssafy.ggame.domain.userTag.dto.UserTagDto;

import java.util.List;

public interface UserTagCustomRepository {

    List<UserTagDto> findByUserId(Integer userId);

}
