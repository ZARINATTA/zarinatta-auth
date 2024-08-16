package server.ticket.controller.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import server.enums.StationCode;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TicketSearchRequest {
    @NotBlank
    private StationCode departStation;
    @NotBlank
    private StationCode arriveStation;
    @FutureOrPresent(message = "현재 날짜 이후 여야 합니다.")
    private LocalDate departDate;
    private LocalTime departTime;
    private String trainType;
}
