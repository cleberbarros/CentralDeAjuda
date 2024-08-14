package firstdecision.repository;

import static org.junit.jupiter.api.Assertions.*;

import firstdecision.model.TicketFeedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class TicketFeedbackRepositoryIntegrationTest {

    @Autowired
    private TicketFeedbackRepository ticketFeedbackRepository;

    private TicketFeedback feedback;

    @BeforeEach
    void setUp() {
        feedback = new TicketFeedback();
        feedback.setTicketId(1L);
        feedback.setUserId(1L);
        feedback.setRating(5);
        feedback.setComment("Great service");
        feedback.setCreatedAt(LocalDateTime.now());
        feedback = ticketFeedbackRepository.save(feedback);
    }

    @Test
    void whenFindByTicketId_thenReturnFeedback() {
        // Act
        Optional<TicketFeedback> foundFeedback = ticketFeedbackRepository.findByTicketId(feedback.getTicketId());

        // Assert
        assertTrue(foundFeedback.isPresent(), "Feedback should be found by ticket ID");
        assertEquals(feedback.getComment(), foundFeedback.get().getComment(), "Comments should match");
    }

    @Test
    void whenFindByTicketIdAndFeedbackDoesNotExist_thenReturnEmpty() {
        // Act
        Optional<TicketFeedback> notFoundFeedback = ticketFeedbackRepository.findByTicketId(999L);

        // Assert
        assertFalse(notFoundFeedback.isPresent(), "No feedback should be found for non-existent ticket ID");
    }
}
