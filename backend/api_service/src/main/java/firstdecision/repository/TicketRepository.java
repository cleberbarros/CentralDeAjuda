package firstdecision.repository;


import java.util.Optional;

import firstdecision.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.userId = :userId AND t.id = :ticketId")
    Optional<Ticket> findByUserIdAndTicketId(@Param("userId") Long userId, @Param("ticketId") Long ticketId);

    @Query("SELECT t FROM Ticket t WHERE t.userId = :userId AND LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<Ticket> findByUserIdAndDescriptionContaining(@Param("userId") Long userId,
            @Param("description") String description, Pageable pageable);

    Optional<Ticket> findById(Long ticketId);

    @Query("SELECT t FROM Ticket t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<Ticket> findByDescriptionContaining(@Param("description") String description, Pageable pageable);

}
