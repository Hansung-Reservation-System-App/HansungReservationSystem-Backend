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
}
