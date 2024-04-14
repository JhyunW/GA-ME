package ssafy.ggame.domain.prefer.repository;

import java.util.List;

public interface PreferCustomRepository {
    //선호하는 게임 이름만 가져옴
    List<String> findPreferGameNames(Integer userId);
}
