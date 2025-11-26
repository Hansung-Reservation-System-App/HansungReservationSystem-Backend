package hansung.app.server.domain.history.dto.request;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityHistoryRequest {
    String facilityId;
    String date;
}
