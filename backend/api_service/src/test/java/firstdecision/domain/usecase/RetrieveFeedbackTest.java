package firstdecision.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import firstdecision.model.TicketFeedback;
import firstdecision.repository.TicketFeedbackRepository;
import firstdecision.service.RetrieveFeedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

public class RetrieveFeedbackTest {

    @Mock
    private TicketFeedbackRepository ticketFeedbackRepository;

    @InjectMocks
    private RetrieveFeedback retrieveFeedback;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRetrieveFeedbackByTicketId_thenCorrectFeedbackIsReturned() {
        // Arrange
        TicketFeedback expectedFeedback = new TicketFeedback();
        expectedFeedback.setId(1L);
        expectedFeedback.setTicketId(1L);
        expectedFeedback.setUserId(1L);
        expectedFeedback.setRating(5);
        expectedFeedback.setComment("Excellent service");

        when(ticketFeedbackRepository.findByTicketId(anyLong())).thenReturn(Optional.of(expectedFeedback));

        // Act
        Optional<TicketFeedback> result = retrieveFeedback.execute(1L);

        // Assert
        assertTrue(result.isPresent(), "Expected feedback to be present");
        assertEquals(expectedFeedback, result.get(), "Expected feedback to match the mock response");
    }

    @Test
    void whenRetrieveFeedbackByTicketIdAndFeedbackDoesNotExist_thenNoFeedbackIsReturned() {
        // Arrange
        when(ticketFeedbackRepository.findByTicketId(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<TicketFeedback> result = retrieveFeedback.execute(999L); // Using an unlikely ticket ID for clarity

        // Assert
        assertFalse(result.isPresent(), "Expected no feedback to be returned");
    }
}
