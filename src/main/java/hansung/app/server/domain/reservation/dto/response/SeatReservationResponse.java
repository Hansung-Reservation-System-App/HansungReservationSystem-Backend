package hansung.app.server.domain.reservation.dto.response;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeatReservationResponse {
    private String facilityId; // ✅ 추가됨
    private int seatNumber;
    private Timestamp startTime;
    private Timestamp endTime;
    private boolean active;
}