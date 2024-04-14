package ssafy.ggame.domain.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.ggame.domain.code.entity.Code;
import ssafy.ggame.domain.tag.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, String> {
    Optional<Code> findByCodeId(String codeId);

}
