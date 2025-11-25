package hansung.app.server.domain.sensor.dto.request;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSensorRequest {
    String facilityId;
    String direction;
    int countChange;
    int resultCount;
}
