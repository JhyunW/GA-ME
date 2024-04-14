package ssafy.ggame.domain.userActionLog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@PrimaryKeyClass
public class UserActionLogKey {

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Integer userId;

    @PrimaryKeyColumn(name = "action_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private LocalDateTime actionTime;

    public UserActionLogKey(Integer userId, LocalDateTime time) {
        this.userId = userId;
        this.actionTime = time;
    }
}