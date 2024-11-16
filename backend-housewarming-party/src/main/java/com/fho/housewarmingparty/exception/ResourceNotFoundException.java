package com.fho.housewarmingparty.exception;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 1676520396492187092L;
    private static final String DEFAULT_MESSAGE = "exception.resource-not-found";
    private final String resource;
    private final Serializable id;

    public ResourceNotFoundException(MessageSource messageSource, String resource, Serializable id) {
        super(messageSource, ErrorCode.NOT_FOUND, DEFAULT_MESSAGE, resource, id);
        this.resource = resource;
        this.id = id;
    }

    public ResourceNotFoundException(MessageSource messageSource, Class<?> clazz, Serializable id) {
        super(messageSource, ErrorCode.NOT_FOUND, DEFAULT_MESSAGE, clazz.getSimpleName(), id);
        this.resource = clazz.getSimpleName();
        this.id = id;
    }

    public ResourceNotFoundException(MessageSource messageSource, String errorCode, String message) {
        super(messageSource, errorCode, message);
        this.resource = null;
        this.id = null;
    }
}
