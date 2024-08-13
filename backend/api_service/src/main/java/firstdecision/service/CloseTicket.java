package firstdecision.service;

import firstdecision.model.Ticket;
import firstdecision.model.TicketStatus;
import firstdecision.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CloseTicket {

    private final TicketRepository ticketRepository;

    public Optional<Ticket> execute(@NonNull Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent() && ticket.get().getStatus() != TicketStatus.CLOSED) {
            Ticket updatingTicket = ticket.get();
            updatingTicket.setStatus(TicketStatus.CLOSED);
            updatingTicket.setClosedAt(LocalDateTime.now());
            return Optional.of(ticketRepository.save(updatingTicket));
        }
        return Optional.empty();
    }
}
