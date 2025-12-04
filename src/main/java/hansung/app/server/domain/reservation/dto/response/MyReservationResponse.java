package hansung.app.server.domain.reservation.dto.response;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.facility.entity.Facility;
import hansung.app.server.domain.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyReservationResponse {
    private String reservationId;   // 예약 ID
    private String userId;          // 사용자 ID
    private String facilityId;      // 시설 ID
    private String facilityName;    // 시설 이름 (빌딩 이름)
    private int seatNumber;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;

    public static MyReservationResponse createResponseDto(Reservation reservation, Facility facility) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getFacilityId(),
                facility.getName(),
                reservation.getSeatNumber(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus()
        );
    }
}
