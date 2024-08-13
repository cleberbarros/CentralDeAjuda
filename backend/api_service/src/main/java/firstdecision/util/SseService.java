package firstdecision.util;

import firstdecision.dto.TicketDTO;
import firstdecision.dto.TicketFeedbackDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SseService {
    private final Set<SseEmitter> emitters = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));
        emitters.add(emitter);
        return emitter;
    }

    public void sendToAll(Object obj) {
        if (obj == null) {
            return;
        }
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(obj));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    public void sendTicketToAll(TicketDTO ticket) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", "TICKET");
        event.put("data", ticket);
        sendToAll(event);
    }

    public void sendFeedbackToAll(TicketFeedbackDTO feedback) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", "FEEDBACK");
        event.put("data", feedback);
        sendToAll(event);
    }

}
