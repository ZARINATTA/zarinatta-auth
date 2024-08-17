package server.ticket.controller.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
}
