package hansung.app.server.domain.history.exception.code;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HistoryErrorCode implements BaseErrorCode {
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "H4001", "해당 시설의 히스토리를 찾을 수 없습니다."),
    HISTORY_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "H5001", "히스토리 저장 중 오류가 발생했습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "H4001", "날짜 형식이 올바르지 않습니다. 형식: yyyy-MM-dd"),
    HISTORY_QUERY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "H5002", "히스토리 조회 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
