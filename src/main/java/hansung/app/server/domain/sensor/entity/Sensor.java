package hansung.app.server.domain.sensor.entity;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.sensor.dto.request.CreateSensorRequest;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class Sensor {
    private String id;              // 이벤트 고유 ID (UUID)
    private String facilityId;      // 어떤 시설인지
    private String direction;       // "IN" 또는 "OUT"
    private int countChange;        // 입장(+1) / 퇴장(-1)
    private int resultCount;        // 처리 후 시설 최종 인원 수
    private Timestamp eventTime;    // 센서 이벤트 발생 시각

    private Sensor(String id,
                  String facilityId,
                  String direction,
                  int countChange,
                  int resultCount,
                  Timestamp eventTime) {
        this.id = id;
        this.facilityId = facilityId;
        this.direction = direction;
        this.countChange = countChange;
        this.resultCount = resultCount;
        this.eventTime = eventTime;
    }

    public static Sensor createSensor(CreateSensorRequest request){
        return new Sensor(
                UUID.randomUUID().toString(),
                request.getFacilityId(),
                request.getDirection(),
                request.getCountChange(),
                request.getResultCount(),
                Timestamp.now()
        );
    }
}

