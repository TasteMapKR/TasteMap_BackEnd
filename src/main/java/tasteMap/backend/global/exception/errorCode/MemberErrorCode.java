package tasteMap.backend.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode{
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다.");
    private HttpStatus httpStatus;
    private String message;


}
