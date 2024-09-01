package server.bookmark;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.entity.BookMark;
import server.entity.User;
import server.oauth.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static server.enums.Values.HEADER_AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookMarkController {
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;

    @GetMapping("/search")
    public List<BookMarkSearchResponse> searchBookMark(@RequestHeader(value = HEADER_AUTHORIZATION) String authorizationHeader, @RequestParam("ticketIds") List<Long> ticketIds) {
        //todo 헤더의 토큰을 통해 User 정보를 가져오는 로직 추가 필요
        User user = userRepository.findById("1").get();
        List<BookMark> bookMarks = bookMarkRepository.findAllByTicketIdInAndUserId(ticketIds, user);
        List<BookMarkSearchResponse> response = bookMarks.stream()
                .map(bookMark -> BookMarkSearchResponse.of(bookMark.getTicket().getId(), true))
                .collect(Collectors.toList());
        return response;
    }
}
