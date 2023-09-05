package com.example.demo.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(CustomException.class)
  public ErrorResponse handleCustomException(CustomException e) {
    return new ErrorResponse(e.getErrorCode(), e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleNotValidException(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();

    StringBuilder sb = new StringBuilder();
    for(FieldError fieldError: bindingResult.getFieldErrors()) {
      sb.append(fieldError.getDefaultMessage()).append("\n");
    }
    return new ErrorResponse(ErrorCode.INPUT_INVALID, sb.toString());
  }
}
