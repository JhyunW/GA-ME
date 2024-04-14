package ssafy.ggame.domain.userActionLog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.userActionLog.dto.UserActionLogResDto;
import ssafy.ggame.domain.userActionLog.entity.UserActionLog;
import ssafy.ggame.domain.userActionLog.entity.UserActionLogKey;
import ssafy.ggame.domain.userActionLog.repository.UserActionLogRepository;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserActionLogService {

    @Autowired
    private UserActionLogRepository userActionLogRepository;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper 주입

    // 사용자 행동 패턴에 대한 로그
    @Transactional
    public UserActionLogResDto loggingUserTagWeight(Integer userId, String page, String action, List<Map<String, Object>> args) {
        List<String> jsonParams = new ArrayList<>();
        try {
            // 각 요소를 JSON 문자열로 변환하여 jsonParams에 추가합니다.
            for (Map<String, Object> arg : args) {
                String json = objectMapper.writeValueAsString(arg);
                jsonParams.add(json);
            }
        } catch (Exception e) {
            throw new BaseException(StatusCode.JSON_TRANS_FAIL);
        }

        // UserActionLogKey 객체 생성
        UserActionLogKey key = new UserActionLogKey(userId, LocalDateTime.now());

        UserActionLog log = UserActionLog.builder()
                .key(key) // 복합 키 객체 사용
                .pageName(page)
                .actionType(action)
                .actionParams(jsonParams) // JSON 문자열을 포함하는 리스트
                .build();

        userActionLogRepository.save(log); // DB에 저장

        // UserActionLogResponseDto 생성 시 수정된 내용 반영
        UserActionLogResDto responseDto = UserActionLogResDto.builder()
                .userId(log.getKey().getUserId())
                .actionTime(log.getKey().getActionTime())
                .pageName(log.getPageName())
                .actionType(log.getActionType())
                .actionParams(log.getActionParams())
                .build();

        return responseDto;
    }
}