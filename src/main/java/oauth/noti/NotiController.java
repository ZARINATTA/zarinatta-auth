package oauth.noti;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotiController {
    private final NotiService notiService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponseWrapper> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        int result = notiService.sendMessageTo(fcmSendDto);

        ApiResponseWrapper arw = ApiResponseWrapper
                .builder()
                .result(result)
                .resultCode(200)
                .resultMsg("SUCCESS")
                .build();
        return new ResponseEntity<>(arw, HttpStatus.OK);
    }
//    @PostMapping("/new")
//    public void save(@RequestBody String token) {
//            notiService
//    }
}
