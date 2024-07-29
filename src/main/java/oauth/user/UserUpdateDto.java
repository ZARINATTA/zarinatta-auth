package oauth.user;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserUpdateDto {
    @Nullable
    String userDeviceToken;

    @Nullable
    String userPhone;
}
