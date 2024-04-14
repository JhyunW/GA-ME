package ssafy.ggame.global.exception;


import lombok.Getter;
import ssafy.ggame.global.common.StatusCode;

@Getter
public class BaseException extends RuntimeException {
    private final StatusCode statusCode;

    public BaseException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }
}
