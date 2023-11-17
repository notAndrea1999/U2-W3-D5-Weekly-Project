package andreademasi.exceptions;


import andreademasi.payloads.users.ErrorsResponseWithListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsResponseWithListDTO handleBadRequest(BadRequestException e) {
        if (e.getErrorsList() != null) {
            List<String> errorList = e.getErrorsList().stream().map(err -> err.getDefaultMessage()).toList();
            return new ErrorsResponseWithListDTO(e.getMessage(), new Date(), errorList);
        } else {
            return new ErrorsResponseWithListDTO(e.getMessage(), new Date(), new ArrayList<>());
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsPayload handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsPayload(ex.getMessage(), new Date());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsPayload handleAccessDenied(AccessDeniedException ex) {
        return new ErrorsPayload(ex.getMessage(), new Date());
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    public ErrorsPayload handleNotFound(NotFoundException e) {
        return new ErrorsPayload(e.getMessage(), new Date());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    public ErrorsPayload handleGeneric(Exception e) {
        e.printStackTrace();
        return new ErrorsPayload("Problema lato server...giuro che lo risolveremo presto", new Date());
    }
}
