package server.oauth.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private String userNick;
    private String userEmail;
}
