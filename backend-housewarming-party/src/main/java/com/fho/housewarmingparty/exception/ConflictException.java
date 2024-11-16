package com.fho.housewarmingparty.exception;

import java.io.Serial;

import org.springframework.context.MessageSource;

import lombok.Getter;

@Getter
public class ConflictException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 611344655102302244L;

    private final Object value;

    public ConflictException(String errorCode, String message) {
        super(errorCode, message);
        this.value = null;
    }

    public ConflictException(MessageSource messageSource, String errorCode, String message) {
        super(messageSource, errorCode, message);
        this.value = null;
    }

    public ConflictException(MessageSource messageSource, String errorCode, String message, Object value) {
        super(messageSource, errorCode, message, value);
        this.value = value;
    }
}
