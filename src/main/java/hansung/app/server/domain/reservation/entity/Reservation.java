package hansung.app.server.domain.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    private String id;
    private String facilityId;
    private String userId;
    private int seatNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

