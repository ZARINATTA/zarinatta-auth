package oauth.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenRequestDto {
    String grant_type;
    String client_id;
    String code;
    String redirect_uri;
}
