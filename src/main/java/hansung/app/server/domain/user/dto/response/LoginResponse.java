package hansung.app.server.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String userId;
    private String name;
}
