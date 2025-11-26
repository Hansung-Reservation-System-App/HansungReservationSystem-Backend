package hansung.app.server.domain.history.controllerDocs;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.history.dto.request.CreateHistoryRequest;
import hansung.app.server.domain.history.entity.History;
import hansung.app.server.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@Tag(name = "시설 이용 히스토리 API", description = "시설 이용 이력(시간대별 인원수·혼잡도) 조회 및 테스트용 수동 저장 API")
public interface HistoryControllerDocs {

    @Operation(
            summary = "히스토리 수동 저장 (테스트용)",
            description = """
                    테스트 데이터를 위해 히스토리 한 건을 직접 저장하는 API입니다.
                    실제 운영 환경에서는 서버 스케줄러(@Scheduled)에 의해 매 정시 자동으로 저장되며,
                    이 엔드포인트는 개발/테스트용으로만 사용하는 것을 권장합니다.
                    """
    )
    ApiResponse<String> saveHistory(
            @RequestBody(
                    description = "테스트용으로 저장할 히스토리 데이터",
                    required = true
            )
            CreateHistoryRequest request
    ) throws Exception;

    @Operation(
            summary = "시설 + 날짜 기준 히스토리 조회",
            description = """
                    특정 시설과 특정 날짜(Timestamp 기준)에 해당하는 히스토리 목록을 조회합니다.
                    프론트에서는 일반적으로 날짜 선택 후, 해당 날짜의 기록들을 차트용으로 불러올 때 사용합니다.
                    """
    )
    ApiResponse<List<History>> getHistoryByFacilityIdAndDate(
            @Parameter(
                    description = "시설 ID (facilities 컬렉션의 id 필드)",
                    example = "facility1",
                    required = true
            )
            String facilityId,

            @Parameter(
                    description = "조회 기준 날짜((yyyy-MM-dd)). 예: 2025-11-26",
                    example = "2025-11-26",
                    required = true
            )
            String date
    ) throws Exception;

    @Operation(
            summary = "시설 전체 히스토리 조회",
            description = """
                    특정 시설에 대해 저장된 모든 히스토리 데이터를 조회합니다.
                    날짜 필터 없이 전체 이력을 조회할 때 사용하며,
                    프론트에서 기간 필터링/차트 렌더링 등을 추가로 적용할 수 있습니다.
                    """
    )
    ApiResponse<List<History>> getAllHistoryByFacilityId(
            @Parameter(
                    description = "시설 ID (facilities 컬렉션의 id 필드)",
                    example = "facility1",
                    required = true
            )
            String facilityId
    ) throws Exception;
}
