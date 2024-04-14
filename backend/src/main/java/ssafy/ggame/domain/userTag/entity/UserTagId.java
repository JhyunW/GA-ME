package ssafy.ggame.domain.userTag.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import ssafy.ggame.domain.code.entity.Code;
import ssafy.ggame.domain.tag.entity.Tag;
import ssafy.ggame.domain.tag.entity.TagId;
import ssafy.ggame.domain.user.entity.User;

import java.io.Serializable;

@Embeddable
@Data
@Getter
public class UserTagId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"),
            @JoinColumn(name = "code_id", referencedColumnName = "code_id")
    })
    private Tag tag;
}