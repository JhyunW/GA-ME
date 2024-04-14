package ssafy.ggame.domain.userTag.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.gameTag.entity.GameTag;
import ssafy.ggame.domain.tag.entity.Tag;
import ssafy.ggame.domain.tag.entity.TagId;
import ssafy.ggame.domain.tag.repository.TagRepository;
import ssafy.ggame.domain.user.entity.User;
import ssafy.ggame.domain.user.repository.UserRepository;
import ssafy.ggame.domain.userTag.dto.UserTagDislikeReqDto;
import ssafy.ggame.domain.userTag.dto.UserTagResDto;
import ssafy.ggame.domain.userTag.entity.UserTag;
import ssafy.ggame.domain.userTag.entity.UserTagId;
import ssafy.ggame.domain.userTag.repository.UserTagRepository;
import ssafy.ggame.domain.gameTag.repository.GameTagRepository;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTagService {
    private final UserTagRepository userTagRepository;
    private final GameTagRepository gameTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    // 게임에 대한 사용자 행동 패턴 기반 가중치 업데이트
    @Transactional
    public List<UserTagResDto> updateUserTagWeight(Integer userId, Long gameId, String action) {
        List<UserTagResDto> resDto = new ArrayList<>();
        short weightToAdd = determineWeightToAdd(action);

        // 해당 게임의 모든 태그를 조회
        var gameTags = gameTagRepository.findByGame_GameId(gameId);
        for (GameTag gameTag : gameTags) {
            Tag tag = gameTag.getTag();
            TagId tagId = tag.getTagId(); // Tag 엔티티의 TagId 복합 키 객체를 얻습니다.

            // TagId 객체에서 tagId와 codeId를 추출.
            Short tagIdValue = tagId.getTagId(); // 태그 ID
            String codeIdValue = tagId.getCode().getCodeId(); // 코드 ID

            // 사용자 ID, 태그 ID, 코드 ID로 사용자 태그를 조회하거나 새로 생성
            Optional<UserTag> userTagOptional = userTagRepository.findByUserIdAndTagIdAndCodeId(userId, tagIdValue, codeIdValue);
            UserTag userTag = userTagOptional.orElseGet(() -> {
                User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(StatusCode.USER_NOT_FOUND));
                Tag findTag = tagRepository.findByCodeIdAndTagId(codeIdValue, tagIdValue).orElseThrow(() -> new BaseException(StatusCode.TAG_NOT_EXIST));

                // UserTagId 복합 키 인스턴스 생성 및 설정
                UserTagId userTagId = new UserTagId();
                userTagId.setUser(user);
                userTagId.setTag(tag);

                // 새 UserTag 인스턴스 생성 및 초기화
                UserTag newUserTag = new UserTag();
                newUserTag.setUserTagId(userTagId);
                newUserTag.setUserTagWeight((short) 0); // 초기 가중치 예: 0

                return newUserTag;
            });


            // 가중치 업데이트
            short newWeight = (short) (userTag.getUserTagWeight() + weightToAdd);
            userTag.setUserTagWeight(newWeight);

            UserTagResDto subDto = UserTagResDto.builder()
                    .userId(userId)
                    .tagId(tagIdValue)
                    .codeId(codeIdValue)
                    .tagName(userTag.getUserTagId().getTag().getTagName())
                    .userTagWeight(userTag.getUserTagWeight())
                    .build();

            resDto.add(subDto);
            
            userTagRepository.save(userTag); // 변경된 가중치 저장
        }

        return resDto;
    }

    // '관심없음' 태그 가중치 -20
    @Transactional
    public List<UserTagResDto> dislikeUserTagWeight(Integer userId, List<UserTagDislikeReqDto.TagCodePair> tags) throws BaseException {
        List<UserTagResDto> resDto = new ArrayList<>(); // 변경: null 대신 빈 리스트 초기화

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(StatusCode.USER_NOT_FOUND));

        for (UserTagDislikeReqDto.TagCodePair tagPair : tags) {
            Tag tag = tagRepository.findByCodeIdAndTagId(tagPair.getCodeId(), tagPair.getTagId())
                    .orElseThrow(() -> new BaseException(StatusCode.TAG_NOT_EXIST));

            Optional<UserTag> userTagOptional = userTagRepository.findByUserIdAndTagIdAndCodeId(userId, tag.getTagId().getTagId(), tag.getTagId().getCode().getCodeId());

            UserTag userTag = userTagOptional.orElseGet(() -> {
                // UserTagId 복합 키 인스턴스 생성 및 설정
                UserTagId userTagId = new UserTagId();
                userTagId.setUser(user);
                userTagId.setTag(tag);

                // 새 UserTag 인스턴스 생성 및 초기화
                UserTag newUserTag = new UserTag();
                newUserTag.setUserTagId(userTagId);
                newUserTag.setUserTagWeight((short) 0); // 초기 가중치 예: 0

                return newUserTag;
            });

            short newWeight = (short) (userTag.getUserTagWeight() - 20); // '관심 없음' 태그에 대한 가중치 -20
            userTag.setUserTagWeight(newWeight);

            UserTagResDto subDto = UserTagResDto.builder()
                    .userId(userId)
                    .tagId(tag.getTagId().getTagId())
                    .codeId(tag.getTagId().getCode().getCodeId())
                    .tagName(tag.getTagName())
                    .userTagWeight(userTag.getUserTagWeight())
                    .build();
            resDto.add(subDto);

            userTagRepository.save(userTag);
        }

        return resDto;
    }


    private short determineWeightToAdd(String action) {
        short weightToAdd = 0;
        switch (action) {
            case "detail":
                weightToAdd = 1;
                break;
            case "go-steam":
                weightToAdd = 5;
                break;
            case "like":
                weightToAdd = 10;
                break;
            case "unlike":
                weightToAdd = -10;
                break;
            case "relational":
                weightToAdd = 2;
                break;
            case "video-play":
                weightToAdd = 1;
                break;
            case "dislike":
                weightToAdd = -20;
        }
        return weightToAdd;
    }
}


