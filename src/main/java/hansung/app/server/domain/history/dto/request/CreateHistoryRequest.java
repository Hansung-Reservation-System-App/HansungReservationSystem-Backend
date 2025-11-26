package hansung.app.server.domain.history.dto.request;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;


import java.util.Map;

@Getter
@Builder
public class CreateHistoryRequest {
    String facilityId;
    Timestamp date;
    Map<String, Integer> hourlyData;
    String avgCongestion;
}
