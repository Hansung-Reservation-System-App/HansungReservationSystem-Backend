package hansung.app.server.domain.facility.exception.code;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FacilityErrorCode implements BaseErrorCode {
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "F4001", "존재하지 않는 시설입니다."),
    SENSOR_NOT_MATCH(HttpStatus.BAD_REQUEST, "F4002", "해당 센서 ID와 일치하는 시설이 없습니다."),
    INVALID_UPDATE_REQUEST(HttpStatus.BAD_REQUEST, "F4003", "시설 업데이트 요청 값이 유효하지 않습니다."),
    FIRESTORE_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F5001", "Firestore 업데이트 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
