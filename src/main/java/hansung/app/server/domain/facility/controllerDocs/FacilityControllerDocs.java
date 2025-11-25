package hansung.app.server.domain.facility.controllerDocs;

import hansung.app.server.domain.facility.dto.request.SensorUpdateRequest;
import hansung.app.server.domain.facility.dto.response.FacilityDetailResponse;
import hansung.app.server.domain.facility.dto.response.FacilityListResponse;
import hansung.app.server.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "시설 API", description = "시설 조회 관련 API 문서")
public interface FacilityControllerDocs {

    @Operation(summary = "전체 시설 조회", description = "모든 시설 정보를 조회합니다.")
    public ApiResponse<List<FacilityListResponse>> getAllFacilities() throws Exception;

    @Operation(summary = "단일 시설 조회", description = "시설 ID를 통해 시설 상세 정보를 조회합니다.")
    public ApiResponse<FacilityDetailResponse> getFacilityById(@PathVariable String id) throws Exception;
}
