package hansung.app.server.domain.user.service;

import hansung.app.server.domain.user.dto.request.RegisterRequest;
import hansung.app.server.domain.user.dto.response.LoginResponse;
import hansung.app.server.domain.user.entity.User;
import hansung.app.server.domain.user.exception.UserException;
import hansung.app.server.domain.user.exception.code.UserErrorCode;
import hansung.app.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

   //회원가입
    public String register(RegisterRequest request) throws Exception {

        if (userRepository.findByUserId(request.getUserId()) != null) {
            throw new UserException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.createUser(request);

        return userRepository.save(user);
    }

    //로그인
    public LoginResponse login(String userId, String password) throws Exception {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        return LoginResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .build();
    }

    //비밀번호 찾기
    public String searchPassword(String userId, String phoneNumber) throws Exception{
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }
        if (!user.getPhoneNumber().equals(phoneNumber)) {
            throw new UserException(UserErrorCode.PHONE_MISMATCH);
        }

        return user.getPassword();
    }
}
