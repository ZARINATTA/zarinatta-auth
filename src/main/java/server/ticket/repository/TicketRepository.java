package server.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {
}
