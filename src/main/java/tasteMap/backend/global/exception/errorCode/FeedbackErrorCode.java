package tasteMap.backend.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum FeedbackErrorCode implements ErrorCode{
    NOT_FOUND(HttpStatus.NOT_FOUND, "피드백을 찾지 못했습니다."),
    ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 피드백을 작성했습니다.");
    private HttpStatus httpStatus;
    private String message;
}
