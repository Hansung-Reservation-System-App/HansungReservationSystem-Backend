package hansung.app.server.domain.user.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String userId;
    private String password;
}
