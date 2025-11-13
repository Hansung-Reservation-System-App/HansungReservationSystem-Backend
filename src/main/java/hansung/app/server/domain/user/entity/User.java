package hansung.app.server.domain.user.entity;

import hansung.app.server.domain.user.dto.request.RegisterRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class User {
    private String id;
    private String name;
    private String userId;
    private String phoneNumber;
    private String password;
    private LocalDateTime createdAt;

    private User(String id, String name, String userId, String phoneNumber, String password, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.createdAt = createdAt;
    }
    public static User CreateUser(RegisterRequest request){
        return new User(
                UUID.randomUUID().toString(),
                request.getName(),
                request.getUserId(),
                request.getPhoneNumber(),
                request.getPassword(),
                LocalDateTime.now()
                );
    }
}