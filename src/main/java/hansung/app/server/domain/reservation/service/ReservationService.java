package hansung.app.server.domain.reservation.service;


import com.google.cloud.Timestamp;
import hansung.app.server.domain.facility.entity.Facility;
import hansung.app.server.domain.facility.exception.code.FacilityErrorCode;
import hansung.app.server.domain.facility.repository.FacilityRepository;
import hansung.app.server.domain.reservation.dto.request.CreateReservationRequest;
import hansung.app.server.domain.reservation.dto.response.MyReservationResponse;
import hansung.app.server.domain.reservation.dto.response.SeatReservationResponse;
import hansung.app.server.domain.reservation.entity.Reservation;
import hansung.app.server.domain.reservation.exception.ReservationException;
import hansung.app.server.domain.reservation.exception.code.ReservationErrorCode;
import hansung.app.server.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static hansung.app.server.domain.reservation.dto.response.MyReservationResponse.createResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FacilityRepository facilityRepository;

    // 예약 생성
    public Reservation saveReservation(CreateReservationRequest request) throws ExecutionException, InterruptedException {
        try {
            // 한 사람이 같은 시설에 이미 진행 중 예약 있는지 체크
            if (reservationRepository.existsActiveReservationByUserAndFacility(
                    request.getUserId(),
                    request.getFacilityId()
            )) {
                throw new ReservationException(ReservationErrorCode.USER_ALREADY_RESERVED_IN_FACILITY);
            }

            //같은 시설 + 좌석에 이미 진행 중 예약 있는지 체크
            if (reservationRepository.existsActiveReservation(request.getFacilityId(), request.getSeatNumber())) {
                throw new ReservationException(ReservationErrorCode.SEAT_ALREADY_RESERVED);
            }

            //만석 여부 체크
            Facility facility = facilityRepository.findById(request.getFacilityId());
            if (facility.getCurrentCount() >= facility.getMaxCount()) {
                throw new ReservationException(FacilityErrorCode.FACILITY_FULL);
            }

            //예약 생성
            Reservation reservation = Reservation.createReservation(request);
            reservationRepository.save(reservation);

            //시설 현재 인원 +1
            facilityRepository.increaseCurrentCount(request.getFacilityId());
            
            return reservation;
        }catch (ReservationException e) {
            // 도메인 예외는 그대로 던져서 컨트롤러에서 ApiResponse로 래핑
            throw e;
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
            facilityRepository.decreaseCurrentCount(reservation.getFacilityId());
            //reservationRepository.delete(reservationId);
            return reservation;
        } catch (Exception e) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CANCEL_FAILED);
        }
    }

    // 현재 예약된 좌석들 가져오기
    public List<SeatReservationResponse> getReservationSeats(String facilityId) throws ExecutionException, InterruptedException {
        try {
            List<Reservation> reservations = reservationRepository.findReservationsByFacilityId(facilityId);
            if (reservations == null) {
                throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND_EXCEPTION);
            }

            List<SeatReservationResponse> reservedSeats = new ArrayList<>();
            // "진행 중" 상태인 예약만 필터링
            for (Reservation reservation : reservations) {
                if ("진행 중".equals(reservation.getStatus())) {
                    reservedSeats.add(SeatReservationResponse.builder()
                            .facilityId(facilityId)
                            .seatNumber(reservation.getSeatNumber())
                            .startTime(reservation.getStartTime())
                            .endTime(reservation.getEndTime())
                            .active(reservation.isActive())
                            .build());
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

    // 마이 예약 내역
    public List<MyReservationResponse> getMyReservations(String userId) {
        try {
            List<Reservation> reservations = reservationRepository.findByUserId(userId);
            List<MyReservationResponse> result = new ArrayList<>();

            for (Reservation reservation : reservations) {
                Facility facility = facilityRepository.findById(reservation.getFacilityId());
                result.add(MyReservationResponse.createResponseDto(reservation, facility));
            }

            return result;

        } catch (Exception e) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_QUERY_FAILED);
        }
    }

    @Scheduled(cron = "0 */5 * * * ?")  // 매 5분마다 실행 (00,05,10,15,...분 0초)
    public void autoCancelExpiredReservations() {
        try {
            Timestamp now = Timestamp.now();
            List<Reservation> reservations = reservationRepository.autoCancelExpiredReservations(now);

            if (reservations.isEmpty()) {
                return; // 처리할 예약 없음
            }

            //각 예약의 facilityId 기준으로 현재 인원 감소
            for (Reservation r : reservations) {
                String facilityId = r.getFacilityId();
                if (facilityId != null && !facilityId.isEmpty()) {
                    facilityRepository.decreaseCurrentCount(facilityId);
                }
            }

        } catch (Exception e) {
            log.error("[Scheduler] 만료 예약 자동 취소 중 오류 발생: {}", e.getMessage(), e);
            throw new ReservationException(ReservationErrorCode.AUTO_CANCEL_FAILED);
        }
    }
}
