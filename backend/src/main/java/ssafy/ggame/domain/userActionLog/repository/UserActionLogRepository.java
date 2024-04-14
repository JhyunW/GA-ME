package ssafy.ggame.domain.userActionLog.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import ssafy.ggame.domain.userActionLog.entity.UserActionLog;

public interface UserActionLogRepository extends CassandraRepository<UserActionLog, String> {
}
