package hansung.app.server.global.apiPayload;

import hansung.app.server.global.apiPayload.code.BaseErrorCode;
import hansung.app.server.global.apiPayload.code.BaseSucessCode;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final Boolean isSucess; //성공 여부
    private String code;            // HTTP 상태 코드
    private String message;         // 응답 메시지
    private T data;                 // 실제 데이터 (필요 없으면 null)

    // 성공 응답
    public static <T> ApiResponse<T> onSucess(BaseSucessCode code, T data){
        return new ApiResponse<>(true, code.getCode(), code.getMessage(), data);
    }

    //실패 응답
    public static <T> ApiResponse<T> onFailure(BaseErrorCode code, T data) {
        return new ApiResponse<>(false, code.getCode(), code.getMessage(), data);
    }
}
