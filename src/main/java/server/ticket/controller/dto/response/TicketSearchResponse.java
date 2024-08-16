package server.ticket.controller.dto.response;

import lombok.Data;
import server.enums.StationCode;

@Data
public class TicketSearchResponse {
    private String ticketType;
    private String departTime;
    private String arriveTime;
    private StationCode departStation;
    private StationCode arriveStation;
    private String price;
    private Boolean isBookMarked;
}
