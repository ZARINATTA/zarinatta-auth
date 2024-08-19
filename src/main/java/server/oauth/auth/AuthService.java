package server.oauth.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import server.oauth.config.RedisService;
import server.oauth.exception.ZarinattaException;
import server.oauth.exception.ZarinattaExceptionType;
import server.oauth.user.UserInputDto;
import server.oauth.user.UserService;
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

    public RedirectDto redirect() {
        String requestUri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&nonce=" + nonce;

        WebClient webClient = WebClient.create();

        String response = webClient.get()
                .uri(requestUri)
                .exchangeToMono(AuthService::handleResponse)
                .block();

        return RedirectDto.builder().redirectUri(response).build();
    }

    @Transactional
    public TokenResponseDto signup(String code) throws ZarinattaException {
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
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ZarinattaException(ZarinattaExceptionType.KAKAO_SERVER_ERROR)))
                )
                .bodyToMono(LoginDto.class)
                .block();

        String getUserInfoUri = "https://kapi.kakao.com/v2/user/me";

        webClient = WebClient.create();

        KakaoProfileDto kakaoProfileDto = webClient.post()
                .uri(getUserInfoUri)
                .headers(headers -> headers.setBearerAuth(loginDto.getAccess_token()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ZarinattaException(ZarinattaExceptionType.KAKAO_SERVER_ERROR)))
                )
                .bodyToMono(KakaoProfileDto.class)
                .block();

        String email = kakaoProfileDto.getKakao_account().getEmail();
        String nickname = kakaoProfileDto.getKakao_account().getProfile().getNickname();

        String userId = userService.findUserIdByEmail(email);

        if (userId != null) {
            throw new ZarinattaException(ZarinattaExceptionType.INVALID_TOKEN_ERROR);
        }

        String newUserId = userService.save(UserInputDto.builder()
                .userEmail(email)
                .userNick(nickname)
                .build());

        String accessToken = jwtService.createAccessToken(newUserId);
        String refreshToken = jwtService.createRefreshToken();

        redisService.setValue(refreshToken, newUserId, REFRESH_TIME);

        return TokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    // TODO: 있는 User인지만 확인 -> 새로운 refresh, accessToken 발급
    public TokenResponseDto login(String accessToken, String refreshToken) throws ZarinattaException {
        if(!jwtService.isValidToken(accessToken)) {
            if(redisService.getValue(refreshToken).isEmpty()) {
                throw new ZarinattaException(ZarinattaExceptionType.INVALID_TOKEN_ERROR);
            }
        }

        String userId = jwtService.decodeAccessToken(accessToken);

        if(userId == null) {
            throw new ZarinattaException(ZarinattaExceptionType.INVALID_TOKEN_ERROR);
        }

        String newAccessToken = jwtService.createAccessToken(userId);
        String newRefreshToken = jwtService.createRefreshToken();

        redisService.deleteValue(refreshToken);
        redisService.setValue(newRefreshToken, userId, REFRESH_TIME);

        return TokenResponseDto.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();
    }

    public TokenResponseDto authorize(String accessToken, String refreshToken) throws ZarinattaException {
        String userId = redisService.getValue(refreshToken);

        if(userId == null) {
            this.logout(accessToken);
            throw new ZarinattaException(ZarinattaExceptionType.EXPIRED_TOKEN_ERROR);
        }

        String newAccessToken = jwtService.createAccessToken(userId);
        String newRefreshToken = jwtService.createRefreshToken();

        redisService.deleteValue(refreshToken);
        redisService.setValue(newRefreshToken, userId, REFRESH_TIME);

        return TokenResponseDto.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();
    }

    public void logout(String accessToken) throws ZarinattaException {
        String userId = jwtService.decodeAccessToken(accessToken);

        if(userId == null) {
            throw new ZarinattaException(ZarinattaExceptionType.INVALID_TOKEN_ERROR);
        }

        redisService.deleteValue(userId);
    }

    private static Mono<String> handleResponse(ClientResponse response) throws ZarinattaException {
        if (response.statusCode().is3xxRedirection()) {
            // 응답이 리다이렉션일 경우 Location 헤더를 반환합니다.
            return Mono.justOrEmpty(response.headers().header(HttpHeaders.LOCATION).stream().findFirst());
        }

        throw new ZarinattaException(ZarinattaExceptionType.AUTH_SERVER_ERROR);
    }
}
