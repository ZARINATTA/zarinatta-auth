package oauth.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPubService {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisherService redisPublisherService;
    private final RedisSubscriberListener redisSubscriberListener;

    public void pubMessageChannel(String channel, MessageDto messageDto) {
        redisMessageListenerContainer.addMessageListener(redisSubscriberListener, new ChannelTopic(channel));

        redisPublisherService.publish(new ChannelTopic(channel), messageDto);
    }
}
