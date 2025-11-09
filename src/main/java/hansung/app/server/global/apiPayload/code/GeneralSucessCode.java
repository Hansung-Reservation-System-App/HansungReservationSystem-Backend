package hansung.app.server.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneralSucessCode implements BaseSucessCode{
    OK("COMMON200", "요청이 성공적으로 처리되었습니다."),
    CREATED("COMMON201", "리소스가 성공적으로 생성되었습니다.");

    private final String code;
    private final String message;
}
