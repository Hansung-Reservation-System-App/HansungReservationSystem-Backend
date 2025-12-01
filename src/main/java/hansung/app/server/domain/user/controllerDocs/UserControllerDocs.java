package hansung.app.server.domain.user.controllerDocs;

import hansung.app.server.domain.user.dto.request.LoginRequest;
import hansung.app.server.domain.user.dto.request.CreateUserRequest;
import hansung.app.server.domain.user.dto.request.SearchPasswordRequest;
import hansung.app.server.domain.user.dto.request.UpdateUserRequest;
import hansung.app.server.domain.user.dto.response.LoginResponse;
import hansung.app.server.domain.user.dto.response.MyPageInfoResponse;
import hansung.app.server.domain.user.dto.response.UserProfileResponse;
import hansung.app.server.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원 API", description = "회원가입, 로그인, 비밀번호 찾기, 마이페이지, 회원정보 수정 API 문서")
public interface UserControllerDocs {

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 진행합니다.")
    ApiResponse<String> register(@RequestBody CreateUserRequest request) throws Exception;

    @Operation(summary = "로그인", description = "사용자가 로그인하면 userId, 이름이 반환됩니다.")
    ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) throws Exception;

    @Operation(summary = "비밀번호 찾기", description = "아이디, 전화번호 기반으로 비밀번호를 찾습니다.")
    ApiResponse<String> searchPassword(@RequestBody SearchPasswordRequest request) throws Exception;

    @Operation(summary = "마이페이지 정보 조회", description = "userId 기준으로 마이페이지에 표시할 기본 정보를 조회합니다. (총 예약 시간은 분 단위 기준)")
    ApiResponse<MyPageInfoResponse> getUserProfile(@PathVariable String userId) throws Exception;

    @Operation(summary = "회원 정보 수정", description = "마이페이지에서 이름, 비밀번호, 전화번호 등의 회원 정보를 수정합니다.")
    ApiResponse<UserProfileResponse> updateUser(@PathVariable String userId,
                                   @RequestBody UpdateUserRequest request) throws Exception;
}
