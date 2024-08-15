package server.oauth.exception;

import lombok.Getter;

@Getter
public class ZarinattaException extends RuntimeException{
    private ZarinattaExceptionType exceptionType;

    public ZarinattaException(ZarinattaExceptionType exceptionType) {
        super(String.format("[%s]: %s", exceptionType.getHttpStatus(), exceptionType.getDescription()));
        this.exceptionType = exceptionType;
    }
}
