package com.example.todo.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleEntityNotPresentException(Exception exception, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage(), request.getDescription(false));
		 if (exception instanceof EntityNotPresentException) {
			 return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	        }
		 if(exception instanceof IllegalArgumentException) {
			 return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
		 }
		return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
