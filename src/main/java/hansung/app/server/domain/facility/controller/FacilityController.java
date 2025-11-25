package hansung.app.server.domain.facility.controller;

import hansung.app.server.domain.facility.controllerDocs.FacilityControllerDocs;
import hansung.app.server.domain.facility.dto.request.SensorUpdateRequest;
import hansung.app.server.domain.facility.dto.response.FacilityDetailResponse;
import hansung.app.server.domain.facility.dto.response.FacilityListResponse;
import hansung.app.server.domain.facility.service.FacilityService;
import hansung.app.server.global.apiPayload.ApiResponse;
import hansung.app.server.global.apiPayload.code.GeneralSucessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facilities")
public class FacilityController implements FacilityControllerDocs {
    private final FacilityService facilityService;

    //전체 시설 조회
    @GetMapping
    public ApiResponse<List<FacilityListResponse>> getAllFacilities() throws Exception {
        List<FacilityListResponse> facilities = facilityService.getAllFacilities();

        return ApiResponse.onSucess(GeneralSucessCode.OK, facilities);
    }

    //단일 시설 조회
    @GetMapping("/{id}")
    public ApiResponse<FacilityDetailResponse> getFacilityById(@PathVariable String id) throws Exception {
        FacilityDetailResponse facility = facilityService.getFacilityById(id);

        return ApiResponse.onSucess(GeneralSucessCode.OK, facility);
    }
}
