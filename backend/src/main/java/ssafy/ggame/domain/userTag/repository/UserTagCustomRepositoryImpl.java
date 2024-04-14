package ssafy.ggame.domain.userTag.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ssafy.ggame.domain.tag.dto.TagDto;
import ssafy.ggame.domain.userTag.dto.UserTagDto;

import java.util.List;

import static ssafy.ggame.domain.tag.entity.QTag.tag;
import static ssafy.ggame.domain.user.entity.QUser.user;
import static ssafy.ggame.domain.userTag.entity.QUserTag.userTag;


@Repository
@RequiredArgsConstructor
public class UserTagCustomRepositoryImpl implements UserTagCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserTagDto> findByUserId(Integer userId) {


        return queryFactory.select(
                        Projections.constructor(
                                UserTagDto.class,
                                user.userId.as("userId"),
                                tag.tagId.tagId.as("tagId"),
                                tag.tagId.code.codeId.as("codeId"),
                                tag.tagName.as("tagName"),
                                userTag.userTagWeight.as("userTagWeight")

                        )
                ).from(userTag)
                .join(userTag.userTagId.user, user)
                .join(userTag.userTagId.tag, tag)
                .where(userTag.userTagId.user.userId.eq(userId))
                .fetch();
    }
}
