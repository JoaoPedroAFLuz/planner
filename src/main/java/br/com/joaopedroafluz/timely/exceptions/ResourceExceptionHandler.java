package br.com.joaopedroafluz.timely.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Log4j2
@RestControllerAdvice
@AllArgsConstructor
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleObjectNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return handleCustomException(HttpStatus.NOT_FOUND, "Recurso não encontrado", e.getMessage(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleResourceNotFoundException(HttpServletRequest request) {
        return handleCustomException(HttpStatus.NOT_FOUND, "Recurso não encontrado", "Recurso não encontrado", request);
    }


    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleInvalidRequestException(InvalidRequestException exception,
                                                          HttpServletRequest request) {
        return handleCustomException(HttpStatus.BAD_REQUEST, "Requisição inválida", exception.getMessage(), request);
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleAuthenticationException(Exception ex, HttpServletRequest request) {
        var defaultMessage = "Full authentication is required to access this resource";
        var message = ex.getMessage().equals(defaultMessage) ? "Acesso não autorizado" : ex.getMessage();

        return handleCustomException(HttpStatus.UNAUTHORIZED, "Acesso não autorizado", message, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleException(Exception e, HttpServletRequest request) {
        return handleCustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                "Ocorreu um erro desconhecido contate o administrador do sistema", request);
    }

    private ErrorResponseDTO handleCustomException(HttpStatus status, String errorMessage,
                                                   String message, HttpServletRequest request) {
        return ErrorResponseDTO.builder()
                .status(status.value())
                .error(errorMessage)
                .message(message)
                .path(request.getRequestURI())
                .build();
    }

}
