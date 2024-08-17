package server.ticket.controller.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import server.enums.StationCode;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
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
