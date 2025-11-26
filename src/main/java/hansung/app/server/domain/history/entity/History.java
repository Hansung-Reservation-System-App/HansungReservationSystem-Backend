package hansung.app.server.domain.history.entity;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.history.dto.request.CreateHistoryRequest;
import lombok.*;


import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class History {
    private String id;                          // 기록 ID
    private String facilityId;                  // 시설 ID
    private Timestamp date;                     // 날짜 (YYYY-MM-DD 형식)
    private Map<String, Integer> hourlyData;    // 시간별 데이터 (시간: 인원수)
    private String avgCongestion;               // 평균 혼잡도 (여유, 보통, 혼잡 등)
    private Timestamp createdAt;                // 생성일자

    private History(String id,
                   String facilityId,
                   Timestamp date,
                   Map<String, Integer> hourlyData,
                   String avgCongestion,
                   Timestamp createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.date = date;
        this.hourlyData = hourlyData;
        this.avgCongestion = avgCongestion;
        this.createdAt = createdAt;
    }

    public static History createHistory(CreateHistoryRequest request) {
        return new History(
                request.getFacilityId() + "_" + request.getDate(),
                request.getFacilityId(),
                request.getDate(),
                request.getHourlyData(),
                request.getAvgCongestion(),
                Timestamp.now()
        );
    }
}

