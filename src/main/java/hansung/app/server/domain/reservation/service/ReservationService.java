package hansung.app.server.domain.reservation.service;


import com.google.cloud.Timestamp;
import hansung.app.server.domain.reservation.dto.request.CreateReservationRequest;
import hansung.app.server.domain.reservation.entity.Reservation;
import hansung.app.server.domain.reservation.exception.ReservationException;
import hansung.app.server.domain.reservation.exception.code.ReservationErrorCode;
import hansung.app.server.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    // 예약 생성
    public Reservation saveReservation(CreateReservationRequest request) throws ExecutionException, InterruptedException {
        try {
            Reservation reservation = Reservation.createReservation(request);
            reservationRepository.save(reservation);
            return reservation;
        } catch (Exception e) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CREATE_FAILED);
        }
    }

    // 예약 취소 (상태 변경)
    public Reservation cancelReservation(String reservationId) throws ExecutionException, InterruptedException {
        try {
            Reservation reservation = reservationRepository.findById(reservationId);
            if (reservation == null) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND_EXCEPTION);
            }

            reservationRepository.updateStatus(reservationId, "취소");
            reservationRepository.delete(reservationId);
            return reservation;
        } catch (Exception e) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CANCEL_FAILED);
        }
    }

    // 현재 예약된 좌석들 가져오기
    public List<Integer> getReservationSeats(String facilityId) throws ExecutionException, InterruptedException {
        try {
            List<Reservation> reservations = reservationRepository.findReservationsByFacilityId(facilityId);
            if (reservations == null) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND_EXCEPTION);
            }

            List<Integer> reservedSeats = new ArrayList<>();
            // "진행 중" 상태인 예약만 필터링
            for (Reservation reservation : reservations) {
                if ("진행 중".equals(reservation.getStatus())) {
                    reservedSeats.add(reservation.getSeatNumber()); // 예약된 좌석 번호 추가
                }
            }
            return reservedSeats; // 예약된 좌석 번호 리스트 반환
        } catch (Exception e) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_SEAT_FETCH_FAILED);
        }
    }

    // 예약 연장
    public Reservation extendReservation(String reservationId) throws ExecutionException, InterruptedException {
        try {
            Reservation reservation = reservationRepository.findById(reservationId);
            if (reservation == null) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND_EXCEPTION);
            }

            // 연장할 시간 (2시간 고정) 계산 (기존 종료 시간에 2시간 더하기)
            Instant extendedInstant = reservation.getEndTime().toDate().toInstant().plus(2, ChronoUnit.HOURS);
            Timestamp extendedEndTime = Timestamp.of(Date.from(extendedInstant));

            // 예약 정보 업데이트 (연장된 종료 시간 반영)
            reservationRepository.updateEndTime(reservationId, extendedEndTime);

            // DTO 재생성 후 엔티티 반환
            CreateReservationRequest request = CreateReservationRequest.builder()
                    .facilityId(reservation.getFacilityId())
                    .userId(reservation.getUserId())
                    .seatNumber(reservation.getSeatNumber())
                    .startTime(reservation.getStartTime())
                    .endTime(extendedEndTime)
                    .build();

            return Reservation.createReservation(request);
        } catch (Exception e) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_EXTEND_FAILED);
        }
    }
}
