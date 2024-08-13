package firstdecision.service;

import firstdecision.model.Role;
import firstdecision.model.Ticket;
import firstdecision.model.User;
import firstdecision.repository.TicketRepository;
import firstdecision.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilterTickets {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;



    public Page<Ticket> execute(Long userId, String filter, Pageable pageable) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRoles().contains(Role.MANAGER)) {
                if (filter != null && filter.matches("\\d+")) {
                    Long ticketId = Long.parseLong(filter);
                    Optional<Ticket> ticket = ticketRepository.findById(ticketId);
                    List<Ticket> ticketList = ticket.map(Collections::singletonList).orElseGet(Collections::emptyList);
                    return new PageImpl<>(ticketList, pageable, ticketList.size());
                } else if (filter != null) {
                    return ticketRepository.findByDescriptionContaining(filter, pageable);
                } else {
                    return ticketRepository.findByDescriptionContaining("", pageable);
                }
            } else {
                if (filter != null && filter.matches("\\d+")) {
                    Long ticketId = Long.parseLong(filter);
                    Optional<Ticket> ticket = ticketRepository.findByUserIdAndTicketId(userId, ticketId);
                    List<Ticket> ticketList = ticket.map(Collections::singletonList).orElseGet(Collections::emptyList);
                    return new PageImpl<>(ticketList, pageable, ticketList.size());
                } else if (filter != null) {
                    return ticketRepository.findByUserIdAndDescriptionContaining(userId, filter, pageable);
                } else {
                    return ticketRepository.findByUserIdAndDescriptionContaining(userId, "", pageable);
                }
            }
        }
        throw new RuntimeException("User not found");
    }

}
