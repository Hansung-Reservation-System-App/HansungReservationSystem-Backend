package hansung.app.server.domain.reservation.exception.code;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements BaseErrorCode {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "R4001", "예약을 찾을 수 없습니다."),
    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "R4002", "잘못된 예약 시간입니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "R4003", "이미 예약된 시간입니다."),
    RESERVATION_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "R4001", "존재하지 않는 예약입니다."),
    RESERVATION_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "R5000", "예약 생성 중 오류가 발생했습니다."),
    RESERVATION_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "R5001", "예약 취소 중 오류가 발생했습니다."),
    RESERVATION_SEAT_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "R5002", "예약 좌석 조회 중 오류가 발생했습니다."),
    RESERVATION_EXTEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "R5003", "예약 연장 중 오류가 발생했습니다."),
    INVALID_SEAT_NUMBER(HttpStatus.BAD_REQUEST, "R4004", "잘못된 좌석 번호입니다."),
    RESERVATION_CONFLICT(HttpStatus.CONFLICT, "R4005", "다른 예약과 시간이 겹칩니다."),
    INVALID_RESERVATION_ID(HttpStatus.BAD_REQUEST, "R4006", "잘못된 예약 ID입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

