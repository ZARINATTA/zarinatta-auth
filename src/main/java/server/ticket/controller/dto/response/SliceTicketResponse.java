package server.ticket.controller.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SliceTicketResponse {
    private List<TicketSearchResponse> ticketSearchResponseList;
    private int size;
    private boolean hasNextSlice;
}
