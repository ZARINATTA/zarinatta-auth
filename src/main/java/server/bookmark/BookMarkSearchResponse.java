package server.bookmark;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookMarkSearchResponse {
    private Long ticketId;
    private boolean isBookMarked;

    @Builder
    public BookMarkSearchResponse(Long ticketId, boolean isBookMarked) {
        this.ticketId = ticketId;
        this.isBookMarked = isBookMarked;
    }

    public static BookMarkSearchResponse of(Long ticketId, boolean isBookMarked) {
        return BookMarkSearchResponse.builder()
                .ticketId(ticketId)
                .isBookMarked(isBookMarked)
                .build();
    }
}
