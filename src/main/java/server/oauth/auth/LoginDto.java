package server.oauth.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginDto {
    String token_type;
    String id_token;
    String access_token;
    int expires_in;
    String refresh_token;
    int refresh_token_expires_in;
    String scope;
}
