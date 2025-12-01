package hansung.app.server.domain.user.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import hansung.app.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final Firestore db;

    // 회원가입 (저장)
    public String save(User user) {
        db.collection("users").document(user.getUserId()).set(user);
        return user.getUserId();
    }

    // userId로 단일 유저 조회
    public User findByUserId(String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = db.collection("users").document(userId).get().get();
        return doc.exists() ? doc.toObject(User.class) : null;
    }

    // 전체 유저 조회 (관리자용)
    public List<QueryDocumentSnapshot> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        return future.get().getDocuments();
    }

    // 회원 정보 일부 필드 업데이트
    public void updateUserFields(String userId, Map<String, Object> fields) throws Exception {
        DocumentReference ref = db.collection("users").document(userId);
        ref.update(fields).get();
    }
}

