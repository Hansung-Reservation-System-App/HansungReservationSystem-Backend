package hansung.app.server.domain.facility.service;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.facility.dto.request.FacilityUpdateRequest;
import hansung.app.server.domain.facility.dto.request.SensorUpdateRequest;
import hansung.app.server.domain.facility.dto.response.FacilityDetailResponse;
import hansung.app.server.domain.facility.dto.response.FacilityListResponse;
import hansung.app.server.domain.facility.entity.Facility;
import hansung.app.server.domain.facility.exception.FacilityException;
import hansung.app.server.domain.facility.exception.code.FacilityErrorCode;
import hansung.app.server.domain.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;

    //전체 시설 조회
    public List<FacilityListResponse> getAllFacilities() throws Exception {
        List<Facility> facilities = facilityRepository.findAllFacilities();

        List<FacilityListResponse> responses = new ArrayList<>();

        for (Facility facility : facilities) {
            responses.add(FacilityListResponse.builder()
                    .id(facility.getId())
                    .name(facility.getName())
                    .address(facility.getAddress())
                    .congestionLevel(facility.getCongestionLevel())
                    .operatingHours(facility.getOperatingHours())
                    .imageUrl(facility.getImageUrl())
                    .build());
        }

        return responses;
    }

    //단일 시설 조회
    public FacilityDetailResponse getFacilityById(String id) throws Exception {
        Facility facility = facilityRepository.findById(id);

        if (facility == null) {
            throw new FacilityException(FacilityErrorCode.FACILITY_NOT_FOUND);
        }

        FacilityDetailResponse response = new FacilityDetailResponse(
                facility.getId(),
                facility.getName(),
                facility.getAddress(),
                facility.getBuildingNumber(),
                facility.getOperatingHours(),
                facility.getMaxCount(),
                facility.getCurrentCount(),
                facility.getCongestionLevel(),
                facility.getNotice(),
                facility.getRules(),
                facility.getImageUrl(),
                facility.isAvailableReservation()
        );
        return response;
    }


    //센서로부터 인원수, 혼잡도 업데이트 요청
    public void updateCountBySensor(SensorUpdateRequest request) throws Exception {
        if (request.getSensorId() == null || request.getCurrentCount() < 0) {
            throw new FacilityException(FacilityErrorCode.INVALID_UPDATE_REQUEST);
        }

        // 센서 ID로 시설 찾기
        Facility target = facilityRepository.findBySensorId(request.getSensorId());

        if (target == null) {
            throw new FacilityException(FacilityErrorCode.SENSOR_NOT_MATCH);
        }

        // 혼잡도 계산 로직
        String congestion;
        double ratio = (double) request.getCurrentCount() / target.getMaxCount();

        if (ratio < 0.5) congestion = "여유";
        else if (ratio < 0.8) congestion = "보통";
        else congestion = "혼잡";

        // FacilityUpdateRequest 생성
        FacilityUpdateRequest updateRequest = FacilityUpdateRequest.builder()
                .id(target.getId())
                .currentCount(request.getCurrentCount())
                .congestionLevel(congestion)
                .updatedAt(Timestamp.now())
                .build();

        // 트랜잭션 내에서 업데이트
        try {
            facilityRepository.updateFacility(updateRequest);  // 트랜잭션 사용
        } catch (Exception e) {
            throw new FacilityException(FacilityErrorCode.FIRESTORE_UPDATE_FAILED);
        }
    }
}
