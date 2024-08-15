package oauth.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import oauth.exception.ZarinattaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> saveUser(UserInputDto userInputDto) {
        String userId = userService.save(userInputDto);

        return ResponseEntity.ok(Map.of("userId", userId));
    }

    @PostMapping("/users/update")
    public ResponseEntity<Void> savePhoneNumber(HttpServletRequest request, @RequestBody UserUpdateDto userUpdateDto) throws ZarinattaException {
        Cookie[] cookies = request.getCookies();

        String accessToken = cookies[0].getValue();

        userService.update(accessToken, userUpdateDto);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
