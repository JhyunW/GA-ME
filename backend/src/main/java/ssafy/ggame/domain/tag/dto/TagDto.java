package ssafy.ggame.domain.tag.dto;

import lombok.*;

import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagDto {
    String codeId;
    Short tagId;
    String tagName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TagDto other = (TagDto) obj;
        return Objects.equals(codeId, other.codeId)
                && Objects.equals(tagId, other.tagId)
                && Objects.equals(tagName, other.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeId, tagId, tagName);
    }

//    public TagDto(String codeId, Short tagId, String tagName){
//        this.codeId = codeId;
//        this.tagId = tagId;
//        this.tagName = tagName;
//    }
}
