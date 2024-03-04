package blibliwarmupproject.blogg.controller;

import blibliwarmupproject.blogg.Exception.InvalidRequestException;
import blibliwarmupproject.blogg.Exception.NotFoundException;
import blibliwarmupproject.blogg.model.response.BaseErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseErrorResponse handleInvalidRequest(InvalidRequestException exception) {
        return BaseErrorResponse.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .status(HttpStatus.BAD_REQUEST.name())
            .errors(exception.getMessage())
            .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseErrorResponse handleEmptyResponse(NotFoundException exception) {
        return BaseErrorResponse.builder()
            .code(HttpStatus.NOT_FOUND.value())
            .status(HttpStatus.NOT_FOUND.name())
            .errors(exception.getMessage())
            .build();
    }

}
