package hansung.app.server.domain.sensor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {
    private String id;
    private String facilityId;
    private String direction;
    private LocalDateTime timestamp;
    private int currentCount;
    private String status;
}

