package hansung.app.server.domain.user.service;

import hansung.app.server.domain.reservation.entity.Reservation;
import hansung.app.server.domain.reservation.repository.ReservationRepository;
import hansung.app.server.domain.user.dto.request.CreateUserRequest;
import hansung.app.server.domain.user.dto.request.UpdateUserRequest;
import hansung.app.server.domain.user.dto.response.LoginResponse;
import hansung.app.server.domain.user.dto.response.MyPageInfoResponse;
import hansung.app.server.domain.user.dto.response.UserProfileResponse;
import hansung.app.server.domain.user.entity.User;
import hansung.app.server.domain.user.exception.UserException;
import hansung.app.server.domain.user.exception.code.UserErrorCode;
import hansung.app.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

   //회원가입
    public String register(CreateUserRequest request) throws Exception {

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

    // 마이페이지용 회원 정보 조회
    public MyPageInfoResponse getMyPageInfo(String userId) throws Exception {
        // 1) 유저 조회
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        // 2) 해당 유저의 모든 예약 조회
        List<Reservation> reservations = reservationRepository.findByUserId(userId);

        // 3) 취소 제외 / 총 예약 횟수 + 총 이용시간(분) 계산
        int totalCount = 0;
        long totalMinutes = 0L;

        for (Reservation r : reservations) {
            // 취소된 예약은 제외
            if ("취소".equals(r.getStatus())) {
                continue;
            }
            totalCount++;

            // endTime 또는 startTime이 null일 수 있으면 방어 로직 추가
            if (r.getStartTime() != null && r.getEndTime() != null) {
                long diffSeconds = r.getEndTime().getSeconds() - r.getStartTime().getSeconds();
                if (diffSeconds > 0) {
                    totalMinutes += diffSeconds / 60; // 분 단위 합산
                }
            }
        }

        return MyPageInfoResponse.builder()
                .name(user.getName())
                .userId(user.getUserId())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .totalUseMinutes(totalMinutes)
                .totalReservationCount(totalCount)
                .build();
    }

    // 회원 정보 수정
    public UserProfileResponse updateUser(String userId, UpdateUserRequest request) throws Exception {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        Map<String, Object> fields = new HashMap<>();
        if (request.getName() != null) {
            fields.put("name", request.getName());
        }
        if (request.getPhoneNumber() != null) {
            fields.put("phoneNumber", request.getPhoneNumber());
        }
        if (request.getPassword() != null) {
            fields.put("password", request.getPassword());
        }

        try {
            if (!fields.isEmpty()) {
                userRepository.updateUserFields(userId, fields);
            }
        } catch (Exception e) {
            throw new UserException(UserErrorCode.USER_UPDATE_FAILED);
        }

        // 수정 후 최신 정보 다시 조회
        User updated = userRepository.findByUserId(userId);

        return UserProfileResponse.builder()
                .userId(updated.getUserId())
                .name(updated.getName())
                .phoneNumber(updated.getPhoneNumber())
                .createdAt(updated.getCreatedAt())
                .build();
    }
}
