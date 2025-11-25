package hansung.app.server.domain.sensor.controllerDocs;

import hansung.app.server.domain.sensor.dto.request.SensorEventRequest;
import hansung.app.server.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "센서 API", description = "센서 이벤트 처리 API")
public interface SensorControllerDocs {

    @Operation(
            summary = "센서 이벤트 처리",
            description = "센서 이벤트를 처리하여 시설의 인원수와 혼잡도를 업데이트하고 로그를 저장합니다."
    )
    ApiResponse<String> sensorEvent(
            @Parameter(description = "센서 이벤트 요청 데이터", required = true) SensorEventRequest request) throws Exception;
}
