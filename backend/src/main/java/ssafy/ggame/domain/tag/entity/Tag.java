package ssafy.ggame.domain.tag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ssafy.ggame.domain.tag.dto.TagDto;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {
    @EmbeddedId
    private TagId tagId;
    @Column(name = "tag_name", length = 30)
    private String tagName;

    public TagDto convertToTagDto(){
        return TagDto.builder()
                .codeId(this.getTagId().getCode().getCodeId())
                .tagId(this.getTagId().getTagId())
                .tagName(this.tagName)
                .build();
    }
}