package hansung.app.server.domain.sensor.dto.request;

import lombok.Getter;

@Getter
public class SensorEventRequest {
    private String sensorId;     // 센서 ID
    private String direction;    // IN / OUT
}