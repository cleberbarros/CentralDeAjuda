package firstdecision.repository;

import firstdecision.model.TicketFeedback;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketFeedbackRepository extends JpaRepository<TicketFeedback, Long> {
    Optional<TicketFeedback> findByTicketId(Long ticketId);
}
