package kots.controller;

import kots.controller.dto.ResponseMessageDto;
import kots.exception.CannotProcessedFileException;
import kots.exception.IncorrectFileTypeException;
import kots.exception.NoFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IncorrectFileTypeException.class)
    public ResponseEntity<ResponseMessageDto> incorrectFileTypeException(IncorrectFileTypeException exception) {
        return new ResponseEntity(new ResponseMessageDto(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoFileException.class)
    public ResponseEntity<ResponseMessageDto> handleNoFileException(NoFileException exception) {
        return new ResponseEntity(new ResponseMessageDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotProcessedFileException.class)
    public ResponseEntity<ResponseMessageDto> handleCannotProcessedFileException(CannotProcessedFileException exception) {
        return new ResponseEntity(new ResponseMessageDto(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
