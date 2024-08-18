package server.ticket.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.enums.StationCode;

@Getter
@NoArgsConstructor
public class TicketSearchRequest {
    @NotNull
    private StationCode departStation;
    @NotNull
    private StationCode arriveStation;
    private String departDate;
    private String departTime;
    private String trainType;

    @Builder
    public TicketSearchRequest(StationCode departStation, StationCode arriveStation, String departDate, String departTime, String trainType) {
        this.departStation = departStation;
        this.arriveStation = arriveStation;
        this.departDate = departDate;
        this.departTime = departTime;
        this.trainType = trainType;
    }
}
