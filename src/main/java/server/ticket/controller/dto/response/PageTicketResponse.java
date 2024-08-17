package server.ticket.controller.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PageTicketResponse {
    private List<TicketSearchResponse> responseList;
    private int page;
    private long totalDataCount;
    private int totalPageCount;

    @Builder
    public PageTicketResponse(List<TicketSearchResponse> responseList, int page, long totalDataCount, int totalPageCount) {
        this.responseList = responseList;
        this.page = page;
        this.totalDataCount = totalDataCount;
        this.totalPageCount = totalPageCount;
    }
}
