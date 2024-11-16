package com.fho.housewarmingparty.exception;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String code;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String path;

    @Builder.Default
    private ZonedDateTime datetime = ZonedDateTime.now();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ErrorDetail> details;

    @JsonIgnoreProperties({"cause", "trace", "stackTrace", "localizedMessage", "suppressed"})
    private Throwable error;

    @Getter
    @Builder
    public static class ErrorDetail {
        private String path;
        private String detail;
    }
}
