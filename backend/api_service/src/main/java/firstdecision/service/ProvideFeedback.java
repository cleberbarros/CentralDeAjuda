package firstdecision.service;

import firstdecision.model.TicketFeedback;
import firstdecision.dto.TicketFeedbackDTO;

import firstdecision.repository.TicketFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProvideFeedback {

    private final TicketFeedbackRepository ticketFeedbackRepository;
    private final ModelMapper modelMapper;


    public TicketFeedbackDTO execute(Long userId, Long ticketId, int rating, String comment) {
        TicketFeedback feedback = new TicketFeedback();
        feedback.setUserId(userId);
        feedback.setTicketId(ticketId);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(LocalDateTime.now());
        TicketFeedback savedFeedback = ticketFeedbackRepository.save(feedback);
       // return TicketFeedbackMapper.INSTANCE.ticketFeedbackToTicketFeedbackDTO(savedFeedback);
        return modelMapper.map(savedFeedback, TicketFeedbackDTO.class);
    }
}
