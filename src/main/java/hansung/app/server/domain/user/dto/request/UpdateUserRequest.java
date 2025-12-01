package hansung.app.server.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserRequest {
    private String name;
    private String phoneNumber;
    private String password;
}
