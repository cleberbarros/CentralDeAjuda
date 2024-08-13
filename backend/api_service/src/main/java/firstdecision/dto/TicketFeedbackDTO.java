package firstdecision.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketFeedbackDTO {
    private Long ticketId;
    private Long userId;
    private int rating;
    private String comment;


}
