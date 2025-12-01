package hansung.app.server.domain.user.controller;

import hansung.app.server.domain.user.controllerDocs.UserControllerDocs;
import hansung.app.server.domain.user.dto.request.LoginRequest;
import hansung.app.server.domain.user.dto.request.CreateUserRequest;
import hansung.app.server.domain.user.dto.request.SearchPasswordRequest;
import hansung.app.server.domain.user.dto.request.UpdateUserRequest;
import hansung.app.server.domain.user.dto.response.LoginResponse;
import hansung.app.server.domain.user.dto.response.MyPageInfoResponse;
import hansung.app.server.domain.user.dto.response.UserProfileResponse;
import hansung.app.server.domain.user.service.UserService;
import hansung.app.server.global.apiPayload.ApiResponse;
import hansung.app.server.global.apiPayload.code.GeneralSucessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    //회원가입
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody CreateUserRequest request) throws Exception {
        String userId = userService.register(request);

        return ApiResponse.onSucess(GeneralSucessCode.OK, userId);
    }

    //로그인
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {
        LoginResponse response = userService.login(request.getUserId(), request.getPassword());

        return ApiResponse.onSucess(GeneralSucessCode.OK, response);
    }

    //비밀번호 찾기
    @PostMapping("/search-password")
    public ApiResponse<String> searchPassword(@RequestBody SearchPasswordRequest request)throws Exception {
        String data = userService.searchPassword(request.getUserId(), request.getPhoneNumber());

        return ApiResponse.onSucess(GeneralSucessCode.OK, data);
    }

    // 마이페이지용 회원 정보 조회
    @GetMapping("/{userId}")
    public ApiResponse<MyPageInfoResponse> getUserProfile(@PathVariable String userId) throws Exception {
        MyPageInfoResponse profile = userService.getMyPageInfo(userId);
        return ApiResponse.onSucess(GeneralSucessCode.OK, profile);
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ApiResponse<UserProfileResponse> updateUser(
            @PathVariable String userId,
            @RequestBody UpdateUserRequest request) throws Exception {

        UserProfileResponse updated = userService.updateUser(userId, request);
        return ApiResponse.onSucess(GeneralSucessCode.OK, updated);
    }
}
