package oauth.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import oauth.exception.ZarinattaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) throws ZarinattaException {
        String userId = (String) request.getAttribute("userId");

        userService.delete(userId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/users/update")
    public ResponseEntity<Void> savePhoneNumber(HttpServletRequest request, @RequestBody UserUpdateDto userUpdateDto) throws ZarinattaException {
        String accessToken = (String) request.getAttribute("accessToken");

        userService.update(accessToken, userUpdateDto);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
