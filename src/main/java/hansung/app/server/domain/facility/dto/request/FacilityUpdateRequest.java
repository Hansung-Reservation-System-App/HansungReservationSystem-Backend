package hansung.app.server.domain.facility.dto.request;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityUpdateRequest {
    private String id;
    private int currentCount;
    private String congestionLevel;
    private Timestamp updatedAt;
}
