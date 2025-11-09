package hansung.app.server.domain.user.dto.request;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String name;
    private String userId;
    private String phoneNumber;
    private String password;
}
