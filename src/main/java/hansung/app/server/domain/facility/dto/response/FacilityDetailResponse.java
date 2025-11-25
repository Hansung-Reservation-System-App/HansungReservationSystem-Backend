package hansung.app.server.domain.facility.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FacilityDetailResponse {
    private String id;
    private String name;
    private String address;
    private String buildingNumber;
    private String operatingHours;
    private int maxCount;
    private int currentCount;
    private String congestionLevel;
    private String notice;
    private String rules;
    private String imageUrl;
    private boolean isAvailable;
}

