package oauth.auth;

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

    @GetMapping("/oauth/authorize")
    public ResponseEntity<RedirectDto> authorize() {
        return new ResponseEntity<>(authService.authorize(), HttpStatusCode.valueOf(302));
    }

    @GetMapping("/oauth/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String code) throws Exception {
        TokenResponseDto tokenResponseDto = authService.login(code);

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



    @GetMapping("/test")
    public ResponseEntity<TestDto> test() {
        TestDto testDto = TestDto.builder().message("hello kim!").build();
        return new ResponseEntity<>(testDto, HttpStatusCode.valueOf(200));
    }
}
