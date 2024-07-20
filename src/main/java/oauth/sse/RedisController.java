package oauth.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RedisController {
    private final RedisPublisherService redisPublisherService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam String channel, @RequestParam String message) {
        try {
            ChannelTopic topic = new ChannelTopic(channel);
            redisPublisherService.publish(topic, message);
            return ResponseEntity.ok("Message published -!");
        } catch (Exception e) {
            log.error("Failed to publish message to channel: "+channel, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed");
        }
    }
}
