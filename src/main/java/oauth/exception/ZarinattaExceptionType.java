package oauth.exception;

import lombok.Getter;

@Getter
public enum ZarinattaExceptionType {
    INVALID_USER_REQUEST_ERROR(401, "USER_API_INVALID_REQUEST"),
    INVALID_AUTH_REQUEST_ERROR(401, "AUTH_API_INVALID_REQUEST"),
    INVALID_TOKEN_ERROR(401, "TOKEN_INVALID"),
    EXPIRED_TOKEN_ERROR(401, "TOKEN_EXPIRED"),
    REQUEST_ERROR(400, "BAD REQUEST"),
    KAKAO_SERVER_ERROR(500, "KAKAO_API_RESPONSE_ERROR"),
    AUTH_SERVER_ERROR(500, "AUTH_API_SERVER_ERROR");

    private final int httpStatus;
    private final String description;

    ZarinattaExceptionType(int httpStatus, String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
