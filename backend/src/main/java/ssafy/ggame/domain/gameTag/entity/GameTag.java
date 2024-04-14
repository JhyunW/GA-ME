package ssafy.ggame.domain.gameTag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.web.WebProperties;
import ssafy.ggame.domain.game.entity.Game;
import ssafy.ggame.domain.tag.dto.TagDto;
import ssafy.ggame.domain.tag.entity.Tag;

@Entity
@Getter
@Table(name = "game_tag")
@ToString
public class GameTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_tag_id")
    private Long gameTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"),
            @JoinColumn(name = "code_id", referencedColumnName = "code_id")
    })
    private Tag tag;
    public TagDto convertToTagDto(){
        return TagDto.builder()
                .codeId(tag.getTagId().getCode().getCodeId())
                .tagId(tag.getTagId().getTagId())
                .tagName(tag.getTagName())
                .build();
    }
}
