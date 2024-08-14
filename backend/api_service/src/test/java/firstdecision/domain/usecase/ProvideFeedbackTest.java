package firstdecision.domain.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import firstdecision.model.TicketFeedback;
import firstdecision.dto.TicketFeedbackDTO;
import firstdecision.repository.TicketFeedbackRepository;
import firstdecision.service.ProvideFeedback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class ProvideFeedbackTest {

    @Mock
    private TicketFeedbackRepository ticketFeedbackRepository;

    @InjectMocks
    private ProvideFeedback provideFeedback;

    @Test
    void whenProvideFeedback_thenFeedbackIsSaved() {
        // Arrange
        TicketFeedback mockFeedback = new TicketFeedback();
        mockFeedback.setId(1L);
        mockFeedback.setUserId(1L);
        mockFeedback.setTicketId(1L);
        mockFeedback.setRating(5);
        mockFeedback.setComment("Excellent service");
        mockFeedback.setCreatedAt(LocalDateTime.now());

        when(ticketFeedbackRepository.save(any(TicketFeedback.class))).thenReturn(mockFeedback);

        // Act
        TicketFeedbackDTO result = provideFeedback.execute(1L, 1L, 5, "Excellent service");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getTicketId());
        assertEquals(1L, result.getUserId());
        assertEquals(5, result.getRating());
        assertEquals("Excellent service", result.getComment());
    }
}
