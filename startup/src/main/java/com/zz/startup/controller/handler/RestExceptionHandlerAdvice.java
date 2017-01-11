package com.zz.startup.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandlerAdvice {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
