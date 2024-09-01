package server.oauth.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {
    String grant_type;
    String client_id;
    String code;
    String redirect_uri;
}
