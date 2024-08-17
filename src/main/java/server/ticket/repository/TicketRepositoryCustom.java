package server.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import server.entity.Ticket;
import server.ticket.controller.dto.request.TicketSearchRequest;

public interface TicketRepositoryCustom {
    Page<Ticket> findTicketBySearchDTO(TicketSearchRequest ticketSearchRequest, Pageable pageable);
}
