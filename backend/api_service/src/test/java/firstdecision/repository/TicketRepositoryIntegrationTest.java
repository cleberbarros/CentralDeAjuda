package firstdecision.repository;

import static org.junit.jupiter.api.Assertions.*;

import firstdecision.model.Ticket;
import firstdecision.model.TicketStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test") 
public class TicketRepositoryIntegrationTest {

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setUserId(1L); 
        ticket.setSubject("Sample subject");
        ticket.setDescription("Sample ticket description");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now()); 
        ticketRepository.save(ticket);
    }

    @Test
    void whenFindByUserId_thenPageOfTicketsIsReturned() {
        // Act
        Page<Ticket> result = ticketRepository.findByUserId(ticket.getUserId(), PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(ticket.getUserId(), result.getContent().get(0).getUserId());
    }

    @Test
    void whenFindByUserIdAndTicketId_thenCorrectTicketIsReturned() {
        // Act
        Optional<Ticket> found = ticketRepository.findByUserIdAndTicketId(ticket.getUserId(), ticket.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(ticket.getId(), found.get().getId());
    }

    @Test
    void whenFindByUserIdAndDescriptionContaining_thenPageOfTicketsIsReturned() {
        // Act
        Page<Ticket> result = ticketRepository.findByUserIdAndDescriptionContaining(ticket.getUserId(), "sample",
                PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.getContent().get(0).getDescription().toLowerCase().contains("sample"));
    }
}
