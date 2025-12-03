package hansung.app.server.domain.facility.exception.code;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FacilityErrorCode implements BaseErrorCode {
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "F4001", "존재하지 않는 시설입니다."),
    FIRESTORE_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F5001", "Firestore 업데이트 중 오류가 발생했습니다."),
    DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "F4004", "업데이트할 문서를 찾을 수 없습니다."),
    FACILITY_FULL(HttpStatus.CONFLICT, "F4009", "해당 시설의 정원이 가득 찼습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
