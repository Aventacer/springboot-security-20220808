package com.study.security_jaewon.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.study.security_jaewon.handler.exception.CustomValidationApiException;

@Aspect
@Component
public class ValidationAop {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Pointcut("@annotation(com.study.security_jaewon.handler.aop.annotation.ValidCheck)")
	public void enableValidPointCut() {}
	
	@Before("enableValidPointCut()")
	public void validBeforeCheck(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		LOGGER.info("=======>> 유효성 검사 중");
		
		for(Object arg: args) {
			if(arg.getClass() == BeanPropertyBindingResult.class) {
				
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<String, String>();
					
					bindingResult.getFieldErrors().forEach(error -> {
						errorMap.put(error.getField(), error.getDefaultMessage());
					});
					
					throw new CustomValidationApiException("유효성 검사 실패", errorMap);
				}
			}
		}
	}
	
	@AfterReturning(value = "enableValidPointCut()", returning = "returnObject")
	public void afterReturnCheck(JoinPoint joinPoint, Object returnObject) {
		LOGGER.info("※※유효성 검사 완료 = {}", returnObject);
	}
}
