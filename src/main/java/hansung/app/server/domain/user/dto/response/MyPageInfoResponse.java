package hansung.app.server.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageInfoResponse {

    private String name;              // 사용자 이름
    private String userId;            // 학번(로그인 ID)
    private String phoneNumber;       // 전화번호
    private String password;          // 비밀번호
    
    private long totalUseMinutes;     // 총 이용 시간(분 기준)
    private int totalReservationCount; // 총 예약 횟수
}
