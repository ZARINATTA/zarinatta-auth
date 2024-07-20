package oauth.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriberListener implements MessageListener {
    private final RedisTemplate<String, Object> template;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publicMessage = template.getStringSerializer().deserialize(message.getBody());

            MessageDto messageDto = objectMapper.readValue(publicMessage, MessageDto.class);

            log.info("Redis Subscribe Message : "+publicMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
