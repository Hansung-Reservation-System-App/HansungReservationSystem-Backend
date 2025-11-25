package hansung.app.server.domain.sensor.controller;

import hansung.app.server.domain.sensor.controllerDocs.SensorControllerDocs;
import hansung.app.server.domain.sensor.dto.request.SensorEventRequest;
import hansung.app.server.domain.sensor.service.SensorService;
import hansung.app.server.global.apiPayload.ApiResponse;
import hansung.app.server.global.apiPayload.code.GeneralSucessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors")
public class SensorController implements SensorControllerDocs {

    private final SensorService sensorService;

    @PostMapping("/event")
    public ApiResponse<String> sensorEvent(@RequestBody SensorEventRequest request) throws Exception {
        sensorService.processSensorEvent(request);

        return ApiResponse.onSucess(GeneralSucessCode.OK,"센서 이벤트 처리 완료");
    }
}

