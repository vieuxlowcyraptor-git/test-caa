package fr.alexanj.testapp.controller;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@ControllerAdvice
public class ControllerAdvisor {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> errorHandler(RuntimeException exc) {
		Throwable cause = exc.getCause();
		// we get the message from the cause if available
		if (!Objects.isNull(cause)) {
			exc = (RuntimeException) cause;
		}
		return new ResponseEntity<>(exc.getLocalizedMessage(), HttpStatus.PRECONDITION_FAILED);
	}

}