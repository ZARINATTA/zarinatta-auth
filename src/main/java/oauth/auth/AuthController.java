package oauth.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/oauth/authorize")
    public ResponseEntity<RedirectDto> authorize() {
        return new ResponseEntity<>(authService.authorize(), HttpStatusCode.valueOf(302));
    }

    @GetMapping("/oauth/login")
    public ResponseEntity<TokenDto> login(@RequestParam String code, HttpServletResponse response) {
        LoginDto loginDto = authService.login(code);

        ResponseCookie accessTokenCookie = ResponseCookie.from("skt", loginDto.getAccess_token())
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(loginDto.getExpires_in())
                .build();

        return ResponseEntity.status(HttpStatus.OK).header("Set-Cookie", accessTokenCookie.toString()).body(TokenDto.builder().refreshToken(loginDto.getRefresh_token()).refreshTokenTime(loginDto.getRefresh_token_expires_in()).build());
    }

    @GetMapping("/test")
    public ResponseEntity<TestDto> test() {
        TestDto testDto = TestDto.builder().message("hello kim!").build();
        return new ResponseEntity<>(testDto, HttpStatusCode.valueOf(200));
    }
}
