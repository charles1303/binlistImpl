package com.projects.binlist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import com.projects.binlist.dto.responses.CardDetailDto;

@ControllerAdvice
public class CardDetailControllerAdvise {
	@ExceptionHandler(value = HttpStatusCodeException.class)
	public ResponseEntity<CardDetailDto> handleHttpException(HttpStatusCodeException exception) {
		CardDetailDto response = new CardDetailDto();
		HttpStatus httpStatus = getHttpExceptionDetails(exception);
		response.setDescription(exception.getMessage());
		response.setCode(httpStatus.value());
	      return new ResponseEntity<>(response, httpStatus);
	   }
	
	@ExceptionHandler(value = GeneralException.class)
	public ResponseEntity<CardDetailDto> handleGeneralException(GeneralException exception) {
		CardDetailDto response = new CardDetailDto();
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		response.setDescription(exception.getMessage());
		response.setCode(httpStatus.value());
	      return new ResponseEntity<>(response, httpStatus);
	   }
	
	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<CardDetailDto> handleRecordsNotFoundException(RecordNotFoundException exception) {
		CardDetailDto response = new CardDetailDto();
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		response.setDescription(exception.getMessage());
		response.setCode(httpStatus.value());
	      return new ResponseEntity<>(response, httpStatus);
	   }
	
	private HttpStatus getHttpExceptionDetails(HttpStatusCodeException exception) {
		int code = exception.getRawStatusCode();
		
		switch (code) {
		case 404 : 
			return HttpStatus.NOT_FOUND;
		default:
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}


