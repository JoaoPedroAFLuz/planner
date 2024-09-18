package br.com.joaopedroafluz.timely.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@AllArgsConstructor
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleObjectNotFound(EntityNotFoundException e, HttpServletRequest request) {
        return ErrorResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Recurso não encontrado")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleRequisicaoInvalida(InvalidRequestException e, HttpServletRequest request) {
        return ErrorResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Requisição inválida")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return ErrorResponseDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(e.getMessage())
                .message("Ocorreu um erro desconhecido contate o administrador do sistema")
                .path(request.getRequestURI())
                .build();
    }

}
