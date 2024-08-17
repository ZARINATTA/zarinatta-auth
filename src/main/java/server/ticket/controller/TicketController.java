package server.ticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.ticket.controller.dto.request.TicketSearchRequest;
import server.ticket.controller.dto.response.PageTicketResponse;
import server.ticket.controller.dto.response.TicketSearchResponse;
import server.ticket.service.TicketService;

@Slf4j
@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public PageTicketResponse searchTicket(@Valid TicketSearchRequest ticketSearchRequest, Pageable pageable) {
        return ticketService.getTicket(ticketSearchRequest, pageable);
    }
}
