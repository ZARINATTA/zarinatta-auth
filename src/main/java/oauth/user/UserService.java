package oauth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String save(UserInputDto userInputDto) {
        User user = User.builder()
                .userEmail(userInputDto.getUserEmail())
                .userNick(userInputDto.getUserNick())
                .build();

        return userRepository.save(user).getId();
    }

    public void update(String id, UserUpdateDto userUpdateDto) {
        userRepository.update(id, userUpdateDto.getUserDeviceToken(), userUpdateDto.getUserPhone());
    }

}
