package firstdecision.controller;

import firstdecision.dto.TicketFeedbackDTO;
import firstdecision.repository.TicketFeedbackRepository;
import firstdecision.util.SseService;
import firstdecision.model.TicketFeedback;
import firstdecision.service.ProvideFeedback;
import firstdecision.service.RetrieveFeedback;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final ProvideFeedback provideFeedback;
    private RetrieveFeedback retrieveFeedback;
    private final TicketFeedbackRepository repository;
    private final SseService sseService;

    @Autowired
    public FeedbackController(ProvideFeedback provideFeedback, RetrieveFeedback retrieveFeedback, SseService sseService,
                              TicketFeedbackRepository repository) {
        this.provideFeedback = provideFeedback;
        this.retrieveFeedback = retrieveFeedback;
        this.sseService = sseService;
        this.repository = repository;
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketFeedback> getFeedback(@PathVariable Long ticketId) {
        Optional<TicketFeedback> feedback = retrieveFeedback.execute(ticketId);
        return feedback.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> provideFeedback(@RequestBody TicketFeedbackDTO feedbackDTO) {
        Optional<TicketFeedback> existingFeedback = repository.findByTicketId(feedbackDTO.getTicketId());
        if (existingFeedback.isPresent()) {
            return ResponseEntity.badRequest().body("Feedback already provided for this ticket.");
        }

        TicketFeedbackDTO createdFeedback = provideFeedback.execute(
                feedbackDTO.getUserId(),
                feedbackDTO.getTicketId(),
                feedbackDTO.getRating(),
                feedbackDTO.getComment());

        this.sseService.sendFeedbackToAll(createdFeedback);

        return ResponseEntity.ok(createdFeedback);
    }
}
