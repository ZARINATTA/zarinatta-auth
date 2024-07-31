package oauth.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oauth.config.RedisService;
import oauth.user.UserInputDto;
import oauth.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${KAKAO_CLIENT_ID}")
    private String CLIENT_ID;

    @Value("${KAKAO_REDIRECT_URI}")
    private String REDIRECT_URI;

    @Value("${KAKAO_JWT_NOUNCE}")
    private String nounce;

    private final static long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L; //7일

    private UserService userService;

    private JwtService jwtService;

    private RedisService redisService;

    public RedirectDto authorize() {
        String requestUri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI+"&nounce="+nounce;

        WebClient webClient = WebClient.create();

        String response = webClient.get()
                .uri(requestUri)
                .exchangeToMono(AuthService::handleResponse)
                .block();

        return RedirectDto.builder().redirectUri(response).build();
    }

    @Transactional
    public TokenResponseDto login(String code) throws Exception {
        String requestUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", CLIENT_ID);
        formData.add("code", code);
        formData.add("redirect_uri", REDIRECT_URI);

        WebClient webClient = WebClient.create();

        LoginDto loginDto = webClient.post()
                .uri(requestUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(LoginDto.class)
                .block();

        JwtToken jwtToken = jwtService.decodeKakaoToken(loginDto.getIdToken());

        String userId = userService.findUserIdByEmail(jwtToken.getEmail());

        if(userId.isEmpty()) {
            userId = userService.save(UserInputDto.builder()
                    .userEmail(jwtToken.getEmail())
                    .userNick(jwtToken.getNickname())
                    .build());
        }

        String accessToken = jwtService.createAccessToken(userId);
        String refreshToken = jwtService.createRefreshToken();

        redisService.setValue(userId, loginDto.getRefresh_token(), REFRESH_TIME);

        return TokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private static Mono<String> handleResponse(ClientResponse response) {
        if (response.statusCode().is3xxRedirection()) {
            // 응답이 리다이렉션일 경우 Location 헤더를 반환합니다.
            return Mono.justOrEmpty(response.headers().header(HttpHeaders.LOCATION).stream().findFirst());
        } else {
            // 응답이 리다이렉션이 아닐 경우 빈 Mono를 반환합니다.
            return Mono.empty();
        }
    }
}
