package me.silvernine.tutorial.exception;

import me.silvernine.tutorial.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateMemberException(DuplicateMemberException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .status(409)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundMemberException(NotFoundMemberException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .status(404)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>builder()
                        .status(500)
                        .message("서버 내부 오류 발생")
                        .build());
    }
}