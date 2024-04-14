package ssafy.ggame.domain.code.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Code {
    @Id
    @Column(name = "code_id", length = 3)
    private String codeId;
    @Column(name = "code_name", length = 20)
    private String codeName;
}
