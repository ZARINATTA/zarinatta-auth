package oauth.user;

import lombok.RequiredArgsConstructor;
import oauth.auth.JwtService;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public void update(String accessToken, UserUpdateDto userUpdateDto) throws Exception {
        String userId = jwtService.decodeAccessToken(accessToken);

        userRepository.update(userId, userUpdateDto.getUserDeviceToken(), userUpdateDto.getUserPhone());
    }

    public String findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    public String findUserEmailById(String userId) {
        return userRepository.findUserEmailById(userId);
    }

}
