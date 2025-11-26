package hansung.app.server.domain.history.controller;

import com.google.cloud.Timestamp;
import hansung.app.server.domain.history.controllerDocs.HistoryControllerDocs;
import hansung.app.server.domain.history.dto.request.CreateHistoryRequest;
import hansung.app.server.domain.history.dto.request.FacilityHistoryRequest;
import hansung.app.server.domain.history.service.HistoryService;
import hansung.app.server.domain.history.entity.History;
import hansung.app.server.global.apiPayload.ApiResponse;
import hansung.app.server.global.apiPayload.code.GeneralSucessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController implements HistoryControllerDocs {

    private final HistoryService historyService;

    // 기록 저장
    @PostMapping("/save")
    public ApiResponse<String> saveHistory(@RequestBody CreateHistoryRequest request) throws Exception {
        historyService.saveHistory(request);
        return ApiResponse.onSucess(GeneralSucessCode.OK, "기록이 성공적으로 저장되었습니다.");
    }

    // 시설의 날짜별 기록 조회
    @GetMapping("/facility/{facilityId}/date/{date}")
    public ApiResponse<List<History>> getHistoryByFacilityIdAndDate(
            @PathVariable String facilityId,
            @PathVariable String date) throws Exception {

        FacilityHistoryRequest request=FacilityHistoryRequest.builder()
                .facilityId(facilityId)
                .date(date)
                .build();
        List<History> historyList = historyService.getHistoryByFacilityIdAndDate(request);

        return ApiResponse.onSucess(GeneralSucessCode.OK, historyList);
    }

    // 시설의 모든 기록 조회
    @GetMapping("/facility/{facilityId}")
    public ApiResponse<List<History>> getAllHistoryByFacilityId(@PathVariable String facilityId) throws Exception {
        List<History> historyList = historyService.getHistoryByFacilityId(facilityId);
        return ApiResponse.onSucess(GeneralSucessCode.OK, historyList);
    }
}
