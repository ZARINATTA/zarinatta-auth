package oauth.noti;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 모바일에서 전달받은 객체
 */

@Getter
@ToString
@RequiredArgsConstructor
public class FcmSendDto {
    private String token;

    private String title;

    private String body;
}
