package server.oauth.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RedirectDto {
    String redirectUri;
}
