package server.oauth.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInputDto {
    String userEmail;
    String userNick;
}
