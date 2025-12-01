package hansung.app.server.domain.user.dto.response;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private String userId;       // 학번
    private String name;         // 이름
    private String phoneNumber;  // 전화번호
    private Timestamp createdAt; // 가입일
}
