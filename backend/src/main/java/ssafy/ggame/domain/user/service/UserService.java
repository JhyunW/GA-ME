package ssafy.ggame.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.game.repository.GameCustomRepository;
import ssafy.ggame.domain.gameTag.entity.GameTag;
import ssafy.ggame.domain.prefer.entity.Prefer;
import ssafy.ggame.domain.tag.dto.TagDto;
import ssafy.ggame.domain.user.dto.UserDetailResDto;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.domain.user.repository.UserRepository;
import ssafy.ggame.domain.userTag.dto.UserTagDto;
import ssafy.ggame.domain.userTag.entity.UserTag;
import ssafy.ggame.domain.userTag.repository.UserTagRepository;
import ssafy.ggame.global.common.BaseResponse;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    private final GameCustomRepository gameCustomRepository;

    // 사용자 ID로 사용자 찾기
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    // 이메일로 사용자 찾기
    public Optional<User> findByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    // 마이 페이지
    public ResponseEntity<BaseResponse<UserDetailResDto>> userDetail(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(()->new BaseException(StatusCode.USER_NOT_FOUND));
        // 해당 유저의 좋아요 리스트
        List<Prefer> prefers = user.getPrefers();
        List<GameCardDto> gameCardDtoList = gameCustomRepository.getPreferList(userId);
        // 지금 년도
        int year = LocalDate.now().getYear();

        for(Prefer prefer: prefers) {
            // 유저가 좋아요한 게임 정보
            Game game = prefer.getPreferId().getGame();
            List<TagDto> tagDtoList = new ArrayList<>();
            // 유저가 좋아요한 게임의 태그 정보 리스트
            List<GameTag> gameTagList = game.getGameTags();

            // 유저가 좋아요한 게임의 태그를 TagDto로 변환하는 과정
            for(GameTag gameTag: gameTagList){
                TagDto tagDto = TagDto.builder()
                        .tagId(gameTag.getTag().getTagId().getTagId())
                        .codeId(gameTag.getTag().getTagName())
                        .tagName(gameTag.getGame().getGameName())
                        .build();
                // entity를 dto로 변환 후 리스트에 넣어줌
                tagDtoList.add(tagDto);
            }
        }

        // 해당 유저의 태그 중 가중치 10를 내림차순으로 가져옴
        List<UserTag> userTagList = this.userTagRepository.findFirst10ByUserTagId_UserOrderByUserTagWeightDesc(user);
        List<UserTagDto> userTagDtoList = new ArrayList<>();

        for(UserTag userTag: userTagList){
            // UserTag Entity를 UserTagDto로 변환하는 과정
            UserTagDto userTagDto = UserTagDto.builder()
                    .tagId(userTag.getUserTagId().getTag().getTagId().getTagId())
                    .codeId(userTag.getUserTagId().getTag().getTagId().getCode().getCodeId())
                    .userTagWeight(userTag.getUserTagWeight())
                    .tagName(userTag.getUserTagId().getTag().getTagName())
                    .build();
            // 변환한 dto를 리스트에 넣어줌
            userTagDtoList.add(userTagDto);
        }

        // 위에서 만든 gameCardDtoList, userTagDtoList를 userDetailResDto에 넣어줌
        UserDetailResDto userDetailResDto = UserDetailResDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userProfileImg(user.getUserProfileImg())
                .userAge(year - user.getUserBirth())
                .preferList(gameCardDtoList)
                .tagWeightList(userTagDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(userDetailResDto));
    }
}

