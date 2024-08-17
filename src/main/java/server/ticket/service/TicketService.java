package server.ticket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.entity.Ticket;
import server.ticket.controller.dto.request.TicketSearchRequest;
import server.ticket.controller.dto.response.TicketSearchResponse;
import server.ticket.repository.TicketRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;

    public Page<TicketSearchResponse> getTicket(TicketSearchRequest searchRequest, Pageable pageable) {
        Page<Ticket> ticketBySearchDTO = ticketRepository.findTicketBySearchDTO(searchRequest, pageable);
        List<TicketSearchResponse> content = ticketBySearchDTO.getContent().stream()
                .map(TicketSearchResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, ticketBySearchDTO.getTotalElements());
    }
}
