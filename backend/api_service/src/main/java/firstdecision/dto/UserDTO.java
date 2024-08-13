package firstdecision.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String token;
    private Set<String> roles;


}
