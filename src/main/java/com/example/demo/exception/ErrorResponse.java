package com.example.demo.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
  private ErrorCode errorCode;
  private String message;
}
