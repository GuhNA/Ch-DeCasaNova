package com.fho.housewarmingparty.exception;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ErrorHandlerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorResponse handleConflictException(HttpServletRequest request, ConflictException e) {
        logError(request, e.getMessage());

        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException e) {
        logError(request, e.getMessage());

        String id = Objects.nonNull(e.getId()) ? e.getId().toString() : null;

        List<ErrorResponse.ErrorDetail> errorDetail = new ArrayList<>();
        errorDetail.add(ErrorResponse.ErrorDetail.builder()
                .path(request.getRequestURI())
                .detail(id)
                .build());

        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .details(errorDetail)
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(final HttpServletRequest request, final AccessDeniedException e) {
        logError(request, e.getMessage());

        return ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .code(ErrorCode.ACCESS_DENIED)
                .build();
    }

    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse handleApplicationException(HttpServletResponse response, ApplicationException e) {
        ResponseStatus status = e.getClass().getAnnotation(ResponseStatus.class);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (Objects.nonNull(status)) {
            httpStatus = status.value();
        }
        response.setStatus(httpStatus.value());

        return ErrorResponse.builder()
                .code(String.valueOf(httpStatus.value()))
                .message(e.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleSpringValidationException(HttpServletRequest request, MethodArgumentNotValidException e) {
        logError(request, e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<ErrorResponse.ErrorDetail> errorDetails = fieldErrors.stream()
                .map(this::mapToFieldErrorDetail)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .message("Invalid data sent.")
                .code(ErrorCode.SPRING_VALIDATION)
                .details(errorDetails)
                .build();
    }

    private ErrorResponse.ErrorDetail mapToFieldErrorDetail(FieldError fieldError) {
        return ErrorResponse.ErrorDetail.builder()
                .path(fieldError.getField())
                .detail(String.format("Value: '%s'. Errors: '%s'.",
                        fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
                .build();
    }

    protected void logError(HttpServletRequest request, String exceptionMessage) {
        log.error("Error: '{} - {}'. Exception message: '{}'.", request.getMethod(),
                request.getServletPath(), exceptionMessage);
    }
}
