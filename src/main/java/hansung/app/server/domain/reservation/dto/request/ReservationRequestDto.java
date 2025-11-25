package hansung.app.server.domain.reservation.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.cloud.Timestamp;
import hansung.app.server.domain.config.TimestampDeserializer;
import lombok.*;

@Getter
@Builder
public class ReservationRequestDto {
    private String facilityId;      // 시설 ID
    private String userId;          // 사용자 ID
    private int seatNumber;         // 좌석 번호

    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp startTime;    // 예약 시작 시간 (Timestamp)

    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp endTime;      // 예약 종료 시간 (Timestamp)
}

