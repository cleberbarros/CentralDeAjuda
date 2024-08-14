package firstdecision.domain.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.PageRequest.of;

import firstdecision.model.Role;
import firstdecision.model.Ticket;
import firstdecision.model.User;
import firstdecision.repository.TicketRepository;
import firstdecision.repository.UserRepository;
import firstdecision.service.FilterTickets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

public class FilterTicketsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private FilterTickets filterTickets;

    private User managerUser;
    private User regularUser;
    private Ticket ticket;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize mocks created above
        MockitoAnnotations.openMocks(this);

        managerUser = new User();
        managerUser.setId(1L);
        managerUser.addRole(Role.MANAGER);

        regularUser = new User();
        regularUser.setId(2L);
        regularUser.addRole(Role.USER);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setDescription("Test Ticket");
        ticket.setUserId(2L);

        pageable = of(0, 10);

        when(userRepository.findById(1L)).thenReturn(Optional.of(managerUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.findByDescriptionContaining(anyString(), eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(ticket)));
        when(ticketRepository.findByUserIdAndDescriptionContaining(eq(2L), anyString(), eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(ticket)));
        when(ticketRepository.findByUserIdAndTicketId(eq(2L), eq(1L)))
                .thenReturn(Optional.of(ticket));
    }

    @Test
    void managerShouldFilterTicketsByDescription() {
        Page<Ticket> result = filterTickets.execute(1L, "Test", pageable);
        assertFalse(result.isEmpty());
        assertEquals("Test Ticket", result.getContent().get(0).getDescription());
    }

    @Test
    void userShouldFilterOwnTicketsByDescription() {
        Page<Ticket> result = filterTickets.execute(2L, "Test", pageable);
        assertFalse(result.isEmpty());
        assertEquals("Test Ticket", result.getContent().get(0).getDescription());
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                filterTickets.execute(3L, "Test", pageable));

        assertEquals("User not found", exception.getMessage());
    }

}
