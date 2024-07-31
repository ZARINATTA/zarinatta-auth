package oauth.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtToken {
    String nickname;
    String email;
}