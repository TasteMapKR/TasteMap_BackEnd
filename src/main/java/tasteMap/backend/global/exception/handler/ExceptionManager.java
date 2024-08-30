package tasteMap.backend.global.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.ErrorCode;
import tasteMap.backend.global.response.ResponseDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseDto<?>> appExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ResponseDto<?> response = ResponseDto.fail(errorCode.getHttpStatus().value(), errorCode.getMessage() + " " + e.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 모든 유효성 검사 오류를 수집하여 errors 맵에 추가
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 커스텀 응답 객체 생성
        ResponseDto<Map<String, String>> response = ResponseDto.fail(
            422,  // 422 Unprocessable Entity 상태 코드
            "유효성 검사에 실패했습니다.",
            errors
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
    // HttpRequestMethodNotSupportedException 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ResponseDto<?> response = ResponseDto.fail(
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            "해당 HTTP 메서드는 지원되지 않습니다: " + ex.getMethod()
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    // HttpMediaTypeNotSupportedException 처리
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseDto<?>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        ResponseDto<?> response = ResponseDto.fail(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
            "해당 미디어 타입은 지원되지 않습니다: " + ex.getContentType()
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        // 로그를 남기거나 다른 처리 로직을 추가할 수 있습니다.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("JWT Token has expired");
    }
}
