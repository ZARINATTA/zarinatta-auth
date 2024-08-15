package server.oauth.user;

import lombok.RequiredArgsConstructor;
import server.oauth.auth.JwtService;
import server.oauth.exception.ZarinattaException;
import server.oauth.exception.ZarinattaExceptionType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    public String save(UserInputDto userInputDto) {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .userEmail(userInputDto.getUserEmail())
                .userNick(userInputDto.getUserNick())
                .build();

        return userRepository.save(user).getId();
    }

    public void update(String accessToken, UserUpdateDto userUpdateDto) throws ZarinattaException {
        String userId = jwtService.decodeAccessToken(accessToken);

        if(userId == null) {
            throw new ZarinattaException(ZarinattaExceptionType.INVALID_TOKEN_ERROR);
        }

        userRepository.update(userId, userUpdateDto.getUserDeviceToken(), userUpdateDto.getUserPhone());
    }

    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    public String findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    public String findUserEmailById(String userId) {
        return userRepository.findUserEmailById(userId);
    }

}
