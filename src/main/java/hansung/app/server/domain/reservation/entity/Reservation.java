package hansung.app.server.domain.reservation.entity;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.reservation.dto.request.CreateReservationRequest;
import lombok.*;

import java.util.UUID;


@Getter
@NoArgsConstructor
public class Reservation {
    private String id;                 // 예약 ID
    private String facilityId;         // 시설 ID (시설 엔티티와 연결)
    private String userId;             // 사용자 ID
    private int seatNumber;            // 좌석 번호
    private Timestamp startTime;       // 예약 시작 시간
    private Timestamp endTime;         // 예약 종료 시간
    private String status;             // 예약 상태 (예: "완료", "진행 중", "취소")
    private boolean active;            // 예약 활성화 여부 (true / false)

    private Reservation(String id,
                       String facilityId,
                       String userId,
                       int seatNumber,
                       Timestamp startTime,
                       Timestamp endTime,
                       String status,   // ["진행중", "취소", "완료"]
                       boolean active) {
        this.id = id;
        this.facilityId = facilityId;
        this.userId = userId;
        this.seatNumber = seatNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.active = active;
    }

    public static Reservation createReservation(CreateReservationRequest dto) {
        return new Reservation(
                UUID.randomUUID().toString(),
                dto.getFacilityId(),
                dto.getUserId(),
                dto.getSeatNumber(),
                dto.getStartTime(),
                dto.getEndTime(),
                "진행 중",
                true
        );
    }
}



