package hansung.app.server.domain.user.controller;

import hansung.app.server.domain.user.controllerDocs.UserControllerDocs;
import hansung.app.server.domain.user.dto.request.LoginRequest;
import hansung.app.server.domain.user.dto.request.CreateUserRequest;
import hansung.app.server.domain.user.dto.request.SearchPasswordRequest;
import hansung.app.server.domain.user.dto.response.LoginResponse;
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
}
