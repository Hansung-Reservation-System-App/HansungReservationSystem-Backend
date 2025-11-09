package hansung.app.server.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchPasswordRequest {
    private String userId;
    private String phoneNumber;
}
