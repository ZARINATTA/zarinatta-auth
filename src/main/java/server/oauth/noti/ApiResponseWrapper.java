package server.oauth.noti;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponseWrapper {
    private int result;
    private int resultCode;
    private String resultMsg;
}
