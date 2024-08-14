package firstdecision.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import firstdecision.model.Ticket;
import firstdecision.model.TicketStatus;
import firstdecision.repository.TicketRepository;
import firstdecision.service.CloseTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

public class CloseTicketTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private CloseTicket closeTicket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCloseTicket_thenTicketStatusIsClosed() {
        // Arrange
        Ticket openTicket = new Ticket();
        openTicket.setId(1L);
        openTicket.setStatus(TicketStatus.OPEN);
        openTicket.setCreatedAt(LocalDateTime.now());

        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(openTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Ticket> result = closeTicket.execute( 1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(TicketStatus.CLOSED, result.get().getStatus());
        assertNotNull(result.get().getClosedAt());
    }

    @Test
    void whenCloseTicketThatIsAlreadyClosed_thenTicketRemainsClosed() {
        // Arrange
        Ticket closedTicket = new Ticket();
        closedTicket.setId(1L);
        closedTicket.setStatus(TicketStatus.CLOSED);
        closedTicket.setCreatedAt(LocalDateTime.now());
        closedTicket.setClosedAt(LocalDateTime.now());

        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(closedTicket));

        // Act
        Optional<Ticket> result = closeTicket.execute( 1L);

        // Assert
        assertFalse(result.isPresent(), "Ticket should not be closed again if it is already closed");
    }

    @Test
    void whenCloseNonexistentTicket_thenNoTicketIsClosed() {
        // Arrange
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Ticket> result = closeTicket.execute( 1L);

        // Assert
        assertFalse(result.isPresent(), "No ticket should be closed if it does not exist");
    }
}
