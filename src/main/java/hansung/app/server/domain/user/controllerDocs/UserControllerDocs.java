package hansung.app.server.domain.user.controllerDocs;

import hansung.app.server.domain.user.dto.request.LoginRequest;
import hansung.app.server.domain.user.dto.request.CreateUserRequest;
import hansung.app.server.domain.user.dto.request.SearchPasswordRequest;
import hansung.app.server.domain.user.dto.response.LoginResponse;
import hansung.app.server.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원 API", description = "회원가입, 로그인, 비밀번호 찾기 API 문서")
public interface UserControllerDocs {

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 진행합니다.")
    ApiResponse<String> register(CreateUserRequest request) throws Exception;

    @Operation(summary = "로그인", description = "사용자가 로그인하면 userId, 이름이 반환됩니다.")
    ApiResponse<LoginResponse> login(LoginRequest request) throws Exception;

    @Operation(summary = "비밀번호 찾기", description = "아이디, 전화번호 기반으로 비밀번호를 찾습니다.")
    ApiResponse<String> searchPassword(SearchPasswordRequest request) throws Exception;
}

