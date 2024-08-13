package firstdecision.service;

import firstdecision.model.Ticket;
import firstdecision.model.TicketStatus;
import firstdecision.dto.TicketDTO;
import firstdecision.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CreateTicket {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    public TicketDTO execute(Long userId, String subject, String description) {
        Ticket newTicket = new Ticket();
        newTicket.setUserId(userId);
        newTicket.setSubject(subject);
        newTicket.setDescription(description);
        newTicket.setStatus(TicketStatus.OPEN);
        newTicket.setCreatedAt(LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(newTicket);
        return modelMapper.map(savedTicket, TicketDTO.class);
    }
}
