package oauth.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class AuthService {

    public RedirectDto authorize() {
        String requestUri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;

        WebClient webClient = WebClient.create();

        String response = webClient.get()
                .uri(requestUri)
                .exchangeToMono(AuthService::handleResponse)
                .block();

        return RedirectDto.builder().redirectUri(response).build();
    }

    public LoginDto login(String code) {
        String requestUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", client_id);
        formData.add("code", code);
        formData.add("redirect_uri", redirect_uri);

        WebClient webClient = WebClient.create();

        LoginDto loginDto = webClient.post()
                .uri(requestUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(LoginDto.class)
                .block();

        return loginDto;
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
