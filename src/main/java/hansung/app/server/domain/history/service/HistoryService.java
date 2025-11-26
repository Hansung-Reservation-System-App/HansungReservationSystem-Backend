package hansung.app.server.domain.history.service;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.facility.entity.Facility;
import hansung.app.server.domain.facility.repository.FacilityRepository;
import hansung.app.server.domain.history.dto.request.CreateHistoryRequest;
import hansung.app.server.domain.history.dto.request.FacilityHistoryRequest;
import hansung.app.server.domain.history.entity.History;
import hansung.app.server.domain.history.exception.HistoryException;
import hansung.app.server.domain.history.exception.code.HistoryErrorCode;
import hansung.app.server.domain.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final FacilityRepository facilityRepository;

    // 센서 이벤트로부터 인원수와 혼잡도 기록
    public void saveHistory(CreateHistoryRequest request) {
        try {
            History history = History.createHistory(request);
            historyRepository.save(history); // Firestore async 저장
        } catch (Exception e) {
            throw new HistoryException(HistoryErrorCode.HISTORY_SAVE_FAILED);
        }
    }

    // 시설 ID와 날짜에 따른 기록 조회
    public List<History> getHistoryByFacilityIdAndDate(FacilityHistoryRequest request) throws Exception {
        // 1) "2025-11-26" → LocalDate
        LocalDate localDate;
        try {

            localDate = LocalDate.parse(request.getDate());   // "yyyy-MM-dd"
        } catch (DateTimeParseException e) {
            throw new HistoryException(HistoryErrorCode.INVALID_DATE_FORMAT);
        }

        try {
            ZoneId zone = ZoneId.of("Asia/Seoul");

            // 2) 하루 시작/끝 계산
            ZonedDateTime startZdt = localDate.atStartOfDay(zone);      // 26일 00:00
            ZonedDateTime endZdt   = startZdt.plusDays(1);              // 27일 00:00

            Timestamp startTs = Timestamp.ofTimeSecondsAndNanos(
                    startZdt.toEpochSecond(), 0);
            Timestamp endTs = Timestamp.ofTimeSecondsAndNanos(
                    endZdt.toEpochSecond(), 0);

            // 3) 레포지토리 범위 조회
            List<History> result = historyRepository.findHistoryByFacilityIdAndDate(
                    request.getFacilityId(), startTs, endTs
            );

            if (result == null || result.isEmpty()) {
                throw new HistoryException(HistoryErrorCode.HISTORY_NOT_FOUND);
            }

            return result;

        } catch (Exception e) {
            log.error("날짜와 시설 조회: " + e.getMessage());
            throw new HistoryException(HistoryErrorCode.HISTORY_QUERY_FAILED);
        }
    }

    // 특정 시설의 기록 조회
    public List<History> getHistoryByFacilityId(String facilityId) throws Exception {
        try {
            return historyRepository.findAllHistoryByFacilityId(facilityId);
        } catch (Exception e) {
            throw new HistoryException(HistoryErrorCode.HISTORY_QUERY_FAILED);
        }
    }

    //서버가 켜져 있으면 매 정시마다 실행되는 작업
    // 매 정시마다 실행되는 스케줄러
    @Scheduled(cron = "0 0 * * * ?")  // 매 정시마다 (0분 0초에 실행)
    public void recordHistory() throws Exception {
        List<Facility> facilities = facilityRepository.findAllFacilities();  // 모든 시설 조회

        // 각 시설에 대해 1시간의 데이터를 기록
        for (Facility facility : facilities) {
            Map<String, Integer> hourlyData = collectHourlyData(facility);  // 시간별 데이터 수집
            String avgCongestion = calculateAverageCongestion(facility);  // 혼잡도 계산

            // 기록 저장
            CreateHistoryRequest request=CreateHistoryRequest.builder()
                    .facilityId(facility.getId())
                    .date(Timestamp.now())
                    .hourlyData(hourlyData)
                    .avgCongestion(avgCongestion)
                    .build();

            saveHistory(request);
        }
    }

    // 시간별 데이터 수집
    private Map<String, Integer> collectHourlyData(Facility facility) {
        String currentTime = getCurrentTimeSlot();

        // 해당 시간대의 인원수만 기록
        return Map.of(currentTime, facility.getCurrentCount());
    }

    // 현재 시간대 계산 (정시 기준으로 시간 기록)
    private String getCurrentTimeSlot() {
        // 현재 시간을 기준으로 정시 포맷 구하기 (예: "15:00")
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        return String.format("%02d:00", hour);  // 예시: "15:00"
    }

    // 혼잡도 계산
    private String calculateAverageCongestion(Facility facility) {
        double ratio = (double) facility.getCurrentCount() / facility.getMaxCount();
        if (ratio < 0.5) return "여유";
        else if (ratio < 0.8) return "보통";
        else return "혼잡";
    }
}

