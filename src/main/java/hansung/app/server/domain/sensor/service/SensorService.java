package hansung.app.server.domain.sensor.service;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.facility.dto.request.FacilityUpdateRequest;
import hansung.app.server.domain.facility.entity.Facility;
import hansung.app.server.domain.facility.repository.FacilityRepository;
import hansung.app.server.domain.sensor.dto.request.CreateSensorRequest;
import hansung.app.server.domain.sensor.dto.request.SensorEventRequest;
import hansung.app.server.domain.sensor.entity.Sensor;
import hansung.app.server.domain.sensor.exception.SensorException;
import hansung.app.server.domain.sensor.exception.code.SensorErrorCode;
import hansung.app.server.domain.sensor.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final FacilityRepository facilityRepository;
    private final SensorRepository sensorRepository;

    public void processSensorEvent(SensorEventRequest request) throws Exception {

        // 1) sensorId로 시설 조회
        Facility facility = facilityRepository.findBySensorId(request.getSensorId());
        if (facility == null) {
            throw new SensorException(SensorErrorCode.SENSOR_NOT_MATCH);  // 센서 ID가 매칭되지 않으면 오류 발생
        }

        // 2) 방향에 따른 count 변경
        if (!request.getDirection().equals("IN") && !request.getDirection().equals("OUT")) {
            throw new SensorException(SensorErrorCode.SENSOR_INVALID_DIRECTION);  // 방향이 잘못되었을 경우 오류 발생
        }

        int change = request.getDirection().equals("IN") ? 1 : -1;
        int newCount = Math.max(0, facility.getCurrentCount() + change);

        // 3) 혼잡도 재계산
        String congestion;
        double ratio = (double) newCount / facility.getMaxCount();
        if (ratio < 0.5) congestion = "여유";
        else if (ratio < 0.8) congestion = "보통";
        else congestion = "혼잡";

        // 4) 시설 정보 업데이트
        try {
            facilityRepository.updateFacility(FacilityUpdateRequest.builder()
                    .id(facility.getId())
                    .currentCount(newCount)
                    .congestionLevel(congestion)
                    .updatedAt(Timestamp.now())
                    .build());
        } catch (Exception e) {
            throw new SensorException(SensorErrorCode.SENSOR_UPDATE_FAILED);  // 시설 업데이트 실패 시 오류 발생
        }

        // 5) 센서 이벤트 로그 저장
        CreateSensorRequest sensorData = CreateSensorRequest.builder()
                .facilityId(facility.getId())
                .direction(request.getDirection())
                .countChange(change)
                .resultCount(newCount)
                .build();

        try {
            Sensor sensor = Sensor.createSensor(sensorData);
            sensorRepository.save(sensor);
        } catch (Exception e) {
            throw new SensorException(SensorErrorCode.SENSOR_SAVE_FAILED);  // 센서 로그 저장 실패 시 오류 발생
        }
    }
}
