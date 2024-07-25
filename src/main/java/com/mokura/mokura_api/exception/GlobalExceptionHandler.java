package com.mokura.mokura_api.exception;

import com.mokura.mokura_api.dto.BaseResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleCustomBadRequestException(CustomBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDto<>(e.getMessage(), null));
    }
}
