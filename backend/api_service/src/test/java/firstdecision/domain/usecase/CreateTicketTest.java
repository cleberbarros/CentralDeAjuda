package firstdecision.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import firstdecision.model.Ticket;
import firstdecision.model.TicketStatus;
import firstdecision.dto.TicketDTO;
import firstdecision.repository.TicketRepository;
import firstdecision.service.CreateTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

public class CreateTicketTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private CreateTicket createTicket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCreateTicket_thenTicketIsCreated() {
        // Arrange
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);
        mockTicket.setUserId(1L);
        mockTicket.setSubject("Subject");
        mockTicket.setDescription("Description");
        mockTicket.setStatus(TicketStatus.OPEN);
        mockTicket.setCreatedAt(LocalDateTime.now());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(mockTicket);

        // Act
        TicketDTO result = createTicket.execute(1L, "Subject", "Description");

        // Assert
        assertNotNull(result);
        assertEquals("Subject", result.getSubject());
        assertEquals("Description", result.getDescription());
    }
}
