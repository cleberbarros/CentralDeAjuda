package firstdecision.controller;

import firstdecision.model.Role;
import firstdecision.model.Ticket;
import firstdecision.model.User;
import firstdecision.service.CloseTicket;
import firstdecision.service.CreateTicket;
import firstdecision.service.FilterTickets;
import firstdecision.dto.TicketDTO;
import firstdecision.util.SseService;
import firstdecision.util.UserService;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final CreateTicket createTicket;
    private final CloseTicket closeTicket;
    private final UserService userService;
    private final FilterTickets filterTickets;
    private final SseService sseService;

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        TicketDTO createdTicket = createTicket.execute(ticketDTO.getUserId(), ticketDTO.getSubject(),
                ticketDTO.getDescription());
        sseService.sendTicketToAll(createdTicket);
        return ResponseEntity.ok(createdTicket);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listUserTicketsByFilter(
            @PathVariable Long userId,
            @RequestParam(required = false) String filter,
            @ParameterObject
            @Parameter(description = "Paginação e ordenação",
                    schema = @Schema(implementation = Pageable.class,
                            example = "{\"page\": 0, \"size\": 1, \"sort\": [\"createdAt,asc\"]}"))
            @PageableDefault Pageable pageable) {

        Page<Ticket> tickets = filterTickets.execute(userId, filter, pageable);
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("/{ticketId}/close")
    public ResponseEntity<?> closeTicket(@PathVariable Long ticketId) {
        if (ticketId == null) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserFromAuthentication(authentication);

        if (user.getRoles().contains(Role.MANAGER)) {
            @SuppressWarnings("null")
            Optional<Ticket> closedTicket = closeTicket.execute(ticketId);
            return closedTicket.map(ticket -> ResponseEntity.ok().build())
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
