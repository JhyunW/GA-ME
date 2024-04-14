package ssafy.ggame.domain.userActionLog.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Table("user_actions")
public class UserActionLog {

    @PrimaryKey
    private UserActionLogKey key;

    @Column("page_name") // Cassandra에서 사용하는 @Column 애노테이션
    private String pageName;

    @Column("action_type")
    private String actionType;

    @Column("action_params")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> actionParams;
}
