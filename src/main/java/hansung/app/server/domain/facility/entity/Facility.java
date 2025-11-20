package hansung.app.server.domain.facility.entity;

import com.google.cloud.Timestamp;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility {
    private String id;
    private String name;
    private String address;
    private String buildingNumber;
    private int maxCount;
    private int currentCount;
    private String congestionLevel;
    private String operatingHours;
    private String sensorId;
    private Timestamp updatedAt;
    private String notice;
    private String rules;
    private String imageUrl;

    private Facility(String id,
                    String name,
                    String address,
                    String buildingNumber,
                    int maxCount,
                    int currentCount,
                    String congestionLevel,
                    String operatingHours,
                    String sensorId,
                     Timestamp updatedAt,
                    String notice,
                    String rules,
                    String imageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.buildingNumber = buildingNumber;
        this.maxCount = maxCount;
        this.currentCount = currentCount;
        this.congestionLevel = congestionLevel;
        this.operatingHours = operatingHours;
        this.sensorId = sensorId;
        this.updatedAt = updatedAt;
        this.notice = notice;
        this.rules = rules;
        this.imageUrl = imageUrl;
    }
}
