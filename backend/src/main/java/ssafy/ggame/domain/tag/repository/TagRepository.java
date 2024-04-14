package ssafy.ggame.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.ggame.domain.tag.entity.Tag;
import ssafy.ggame.domain.tag.entity.TagId;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, TagId> {
    @Query("SELECT t FROM Tag t WHERE t.tagId.code.codeId = :codeId AND t.tagId.tagId = :tagId")
    Optional<Tag> findByCodeIdAndTagId(String codeId, Short tagId);
}
