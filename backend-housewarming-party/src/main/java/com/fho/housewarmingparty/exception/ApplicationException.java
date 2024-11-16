package com.fho.housewarmingparty.exception;

import java.io.Serial;

import com.fho.housewarmingparty.utils.language.Language;
import com.fho.housewarmingparty.utils.language.LanguageUtils;
import org.springframework.context.MessageSource;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2182465032829359461L;

    private final String errorCode;

    public ApplicationException(Throwable cause) {
        super(cause);
        this.errorCode = null;
    }

    public ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ApplicationException(MessageSource messageSource, String errorCode, String key, Object... arguments) {
        super(LanguageUtils.getMessage(messageSource, key, Language.PT, arguments));
        this.errorCode = errorCode;
    }
}
