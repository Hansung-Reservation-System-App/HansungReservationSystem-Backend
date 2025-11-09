package hansung.app.server.domain.user.exception.code;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import hansung.app.server.global.apiPayload.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U4001", "존재하지 않는 사용자입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U4002", "이미 존재하는 회원입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U4003", "비밀번호가 일치하지 않습니다."),
    PHONE_MISMATCH(HttpStatus.BAD_REQUEST, "U4004", "전화번호가 일치하지 않습니다."),
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "U005", "존재하지 않는 아이디입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
