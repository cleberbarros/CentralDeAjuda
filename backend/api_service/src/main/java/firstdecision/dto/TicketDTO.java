package firstdecision.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketDTO {
    private Long userId;
    private String subject;
    private String description;


}
