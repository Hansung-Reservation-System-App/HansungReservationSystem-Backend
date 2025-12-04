package hansung.app.server.domain.reservation.controller;

import hansung.app.server.domain.reservation.controllerDocs.ReservationControllerDocs;
import hansung.app.server.domain.reservation.dto.request.CreateReservationRequest;
import hansung.app.server.domain.reservation.dto.response.MyReservationResponse;
import hansung.app.server.domain.reservation.entity.Reservation;
import hansung.app.server.domain.reservation.service.ReservationService;
import hansung.app.server.global.apiPayload.ApiResponse;
import hansung.app.server.global.apiPayload.code.GeneralSucessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController implements ReservationControllerDocs {

    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping
    public ApiResponse<Reservation> createReservation(@RequestBody CreateReservationRequest request) throws Exception {
        Reservation reservation = reservationService.saveReservation(request);
        return ApiResponse.onSucess(GeneralSucessCode.OK, reservation);
    }

    // 예약 취소
    @PutMapping("/cancel/{reservationId}")
    public ApiResponse<Reservation> cancelReservation(@PathVariable String reservationId) throws Exception {
        Reservation reservation = reservationService.cancelReservation(reservationId);
        return ApiResponse.onSucess(GeneralSucessCode.OK, reservation);
    }

    // 시설별 예약 좌석 조회
    @GetMapping("/seats/{facilityId}")
    public ApiResponse<List<Integer>> getReservationSeats(@PathVariable String facilityId) throws Exception {
        List<Integer> reservedSeats = reservationService.getReservationSeats(facilityId);
        return ApiResponse.onSucess(GeneralSucessCode.OK, reservedSeats);
    }

    // 예약 연장
    @PutMapping("/extend/{reservationId}")
    public ApiResponse<Reservation> extendReservation(@PathVariable String reservationId) throws Exception {
        Reservation extendedReservation = reservationService.extendReservation(reservationId);
        return ApiResponse.onSucess(GeneralSucessCode.OK, extendedReservation);
    }

    //마이 예약 내역
    @GetMapping("/my/{userId}")
    public ApiResponse<List<MyReservationResponse>> getMyReservations(@PathVariable String userId) {
        List<MyReservationResponse> reservations = reservationService.getMyReservations(userId);
        return ApiResponse.onSucess(GeneralSucessCode.OK, reservations);
    }
}
