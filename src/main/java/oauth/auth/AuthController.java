package oauth.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/auth/redirect")
    public ResponseEntity<RedirectDto> redirect() {
        return new ResponseEntity<>(authService.redirect(), HttpStatusCode.valueOf(302));
    }

    @GetMapping("/auth/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestParam String code) throws Exception {
        TokenResponseDto tokenResponseDto = authService.signup(code);

        ResponseCookie accessTokenCookie = ResponseCookie.from("skt", tokenResponseDto.getAccessToken())
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(30 * 60 * 1000L)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(Map.of("refreshToken", tokenResponseDto.getRefreshToken()));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(HttpServletRequest request, @RequestBody Map<String, String> map) throws Exception {
        String accessToken = (String) request.getAttribute("accessToken");
        String refreshToken = map.get("refreshToken");

        TokenResponseDto tokenResponseDto = authService.login(accessToken, refreshToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("skt", tokenResponseDto.getAccessToken())
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(30 * 60 * 1000L)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(Map.of("refreshToken", tokenResponseDto.getRefreshToken()));
    }

    @PostMapping("/auth/authorize")
    public ResponseEntity<Map<String, String>> authorize(HttpServletRequest request, @RequestBody Map<String, String> map) throws Exception {
        String accessToken = (String) request.getAttribute("accessToken");
        String refreshToken = map.get("refreshToken");

        TokenResponseDto tokenResponseDto = authService.authorize(accessToken, refreshToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("skt", tokenResponseDto.getAccessToken())
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(30 * 60 * 1000L)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(Map.of("refreshToken", tokenResponseDto.getRefreshToken()));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = (String) request.getAttribute("accessToken");

        authService.logout(accessToken);

        ResponseCookie deletedCookie = ResponseCookie.from("skt", "")
                .maxAge(0) // 쿠키 만료 시간 설정
                .path("/") // 쿠키의 경로 설정
                .httpOnly(true) // 보안 설정
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deletedCookie.toString());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public ResponseEntity<TestDto> test() {
        TestDto testDto = TestDto.builder().message("hello kim!").build();
        return new ResponseEntity<>(testDto, HttpStatusCode.valueOf(200));
    }
}
