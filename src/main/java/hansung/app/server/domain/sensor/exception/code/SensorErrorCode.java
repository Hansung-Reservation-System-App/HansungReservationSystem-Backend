package hansung.app.server.domain.sensor.exception.code;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SensorErrorCode implements BaseErrorCode {
    SENSOR_NOT_MATCH(HttpStatus.NOT_FOUND, "S1001", "매칭되는 센서가 없습니다."),
    SENSOR_INVALID_DIRECTION(HttpStatus.BAD_REQUEST, "S1002", "잘못된 방향(IN/OUT) 값이 입력되었습니다."),
    SENSOR_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S1003", "센서 이벤트 저장 실패."),
    SENSOR_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S1004", "센서 정보 업데이트 실패.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
