package oauth.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    String refreshToken;
    int refreshTokenTime;
}
