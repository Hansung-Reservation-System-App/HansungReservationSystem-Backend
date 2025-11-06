package hansung.app.server.domain.history.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    private String id;
    private String facilityId;
    private String date;
    private Map<String, Integer> hourlyData;
    private String avgCongestion;
    private LocalDateTime createdAt;
}

