package server.oauth.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {
    private static final long serialVersionUid = 1L;

    private String message;
    private String sender;
    private String channelId;
}
