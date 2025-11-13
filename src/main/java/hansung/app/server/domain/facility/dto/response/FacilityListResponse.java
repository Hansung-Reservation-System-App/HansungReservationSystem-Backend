package hansung.app.server.domain.facility.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FacilityListResponse {
    private String id;
    private String name;
    private String address;
    private String congestionLevel;
    private String operatingHours;
    private String imageUrl;
}

