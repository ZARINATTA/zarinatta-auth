package server.ticket.controller.dto.response;

import lombok.Builder;
import lombok.Data;
import server.entity.Ticket;
import server.enums.StationCode;

@Data
public class TicketSearchResponse {
    private Long ticketId;
    private String ticketType;
    private String departTime;
    private String arriveTime;
    private StationCode departStation;
    private StationCode arriveStation;
    private String price;

    @Builder
    public TicketSearchResponse(Long ticketId, String ticketType, String departTime, String arriveTime, StationCode departStation, StationCode arriveStation, String price) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.departStation = departStation;
        this.arriveStation = arriveStation;
        this.price = price;
    }

    public static TicketSearchResponse fromEntity(Ticket ticket) {
        return TicketSearchResponse.builder()
                .ticketId(ticket.getId())
                .ticketType(ticket.getTicketType())
                .departTime(ticket.getDepartTime())
                .arriveTime(ticket.getArriveTime())
                .departStation(ticket.getDepartStation())
                .arriveStation(ticket.getArriveStation())
                .price(ticket.getPrice()).build();
    }
}
