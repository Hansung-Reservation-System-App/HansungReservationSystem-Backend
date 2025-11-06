package hansung.app.server.domain.facility.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {
    private String id;
    private String name;
    private String building;
    private int maxCount;
    private int currentCount;
    private String congestionLevel;
    private String sensorId;
    private LocalDateTime updatedAt;
}
