package firstdecision.service;

import firstdecision.dto.TicketDTO;
import firstdecision.dto.TicketFeedbackDTO;
import firstdecision.util.SseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class SseServiceTest {

    private SseService sseService;

    @BeforeEach
    void setUp() {
        sseService = new SseService();
    }

    @Test
    void createEmitter_ShouldNotThrowException() {
        assertDoesNotThrow(() -> sseService.createEmitter());
    }

    @Test
    void sendToAll_ShouldAttemptToSendDataToEmitters() throws IOException {
        SseEmitter emitter1 = spy(new SseEmitter());
        SseEmitter emitter2 = spy(new SseEmitter());
        sseService.createEmitter();
        sseService.createEmitter();

        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");

        sseService.sendToAll(data);
    }

    @Test
    void sendTicketToAll_ShouldAttemptToSendTicketData() throws IOException {
        sseService.createEmitter();
        sseService.createEmitter();

        TicketDTO ticket = new TicketDTO();
    }

    @Test
    void sendFeedbackToAll_ShouldAttemptToSendFeedbackData() throws IOException {
        sseService.createEmitter();
        sseService.createEmitter();

        TicketFeedbackDTO feedback = new TicketFeedbackDTO();

    }
}
