package hansung.app.server.domain.reservation.controllerDocs;

import hansung.app.server.domain.reservation.dto.request.CreateReservationRequest;
import hansung.app.server.domain.reservation.entity.Reservation;
import hansung.app.server.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "예약 API", description = "좌석 예약, 취소, 연장, 좌석 조회 API 문서")
public interface ReservationControllerDocs {

    @Operation(summary = "예약 생성", description = "좌석 번호, 사용자 ID, 시설 ID를 입력하여 예약을 생성합니다.")
    ApiResponse<Reservation> createReservation(CreateReservationRequest request) throws Exception;

    @Operation(summary = "예약 취소", description = "예약 ID를 통해 예약을 취소합니다.")
    ApiResponse<Reservation> cancelReservation(String reservationId) throws Exception;

    @Operation(summary = "시설별 예약된 좌석 조회", description = "특정 시설의 이미 예약된 좌석 번호 목록을 조회합니다.")
    ApiResponse<List<Integer>> getReservationSeats(String facilityId) throws Exception;

    @Operation(summary = "예약 연장", description = "기존 예약 종료 시간에서 2시간 연장합니다.")
    ApiResponse<Reservation> extendReservation(String reservationId) throws Exception;
}
