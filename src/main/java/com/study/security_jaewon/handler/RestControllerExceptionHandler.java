package com.study.security_jaewon.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.study.security_jaewon.handler.exception.CustomValidationApiException;
import com.study.security_jaewon.web.dto.CMRespDto;

@RestController
@ControllerAdvice
public class RestControllerExceptionHandler {

	@ExceptionHandler(CustomValidationApiException.class)
	private ResponseEntity<?> validationApiException(CustomValidationApiException e){
		return ResponseEntity.badRequest().body(new CMRespDto(-1, e.getMessage(), e.getErrorMessage()));
	}
}
