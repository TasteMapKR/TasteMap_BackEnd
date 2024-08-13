package tasteMap.backend.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum CourseErrorCode implements ErrorCode {
    UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "제거 권한이 없습니다."),
    COURSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Course를 찾을 수 없습니다.");
    private HttpStatus httpStatus;
    private String message;
}
