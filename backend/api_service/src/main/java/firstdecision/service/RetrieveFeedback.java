package firstdecision.service;

import firstdecision.model.TicketFeedback;
import firstdecision.repository.TicketFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RetrieveFeedback {

    private final TicketFeedbackRepository repository;


    public Optional<TicketFeedback> execute(Long ticketId) {
        return repository.findByTicketId(ticketId);
    }
}
