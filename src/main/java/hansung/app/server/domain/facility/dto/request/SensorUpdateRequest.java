package hansung.app.server.domain.facility.dto.request;

import lombok.Getter;

@Getter
public class SensorUpdateRequest {
    private String sensorId;
    private int currentCount;
}
