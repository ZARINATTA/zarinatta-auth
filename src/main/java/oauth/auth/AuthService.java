package oauth.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oauth.config.RedisService;
import oauth.user.UserInputDto;
import oauth.user.UserService;
import org.springframework.beans.factory.annotation.Value;
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

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${KAKAO_CLIENT_ID}")
    private String CLIENT_ID;

    @Value("${KAKAO_REDIRECT_URI}")
    private String REDIRECT_URI;

    @Value("${KAKAO_JWT_NONCE}")
    private String nonce;

    private final static long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L; //7일

    private final UserService userService;

    private final JwtService jwtService;

    private final RedisService redisService;

    public RedirectDto authorize() {
        String requestUri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&nonce=" + nonce;

        WebClient webClient = WebClient.create();

        String response = webClient.get()
                .uri(requestUri)
                .exchangeToMono(AuthService::handleResponse)
                .block();

        return RedirectDto.builder().redirectUri(response).build();
    }

    @Transactional
    public TokenResponseDto login(String code) throws Exception {
        String getTokenUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", CLIENT_ID);
        formData.add("code", code);
        formData.add("redirect_uri", REDIRECT_URI);

        WebClient webClient = WebClient.create();

        LoginDto loginDto = webClient.post()
                .uri(getTokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(LoginDto.class)
                .block();

        String getUserInfoUri = "https://kapi.kakao.com/v2/user/me";

        webClient = WebClient.create();

        KakaoProfileDto kakaoProfileDto = webClient.post()
                .uri(getUserInfoUri)
                .headers(headers -> headers.setBearerAuth(loginDto.getAccess_token()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(KakaoProfileDto.class)
                .block();

        String email = kakaoProfileDto.getKakao_account().getEmail();
        String nickname = kakaoProfileDto.getKakao_account().getProfile().getNickname();

        Optional<String> userId = userService.findUserIdByEmail(email);

        String newUserId;

        if (userId.isEmpty()) {
            newUserId = userService.save(UserInputDto.builder()
                    .userEmail(email)
                    .userNick(nickname)
                    .build());
        } else {
            newUserId = String.valueOf(userId);
        }

        String accessToken = jwtService.createAccessToken(newUserId);
        String refreshToken = jwtService.createRefreshToken();

        redisService.setValue(newUserId, loginDto.getRefresh_token(), REFRESH_TIME);

        return TokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public void logout(String accessToken) throws Exception {
        String userId = jwtService.decodeAccessToken(accessToken);

        redisService.deleteValue(userId);
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
