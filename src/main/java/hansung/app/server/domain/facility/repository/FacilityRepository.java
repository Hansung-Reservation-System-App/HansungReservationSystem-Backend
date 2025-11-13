package hansung.app.server.domain.facility.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import hansung.app.server.domain.facility.dto.request.FacilityUpdateRequest;
import hansung.app.server.domain.facility.entity.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class  FacilityRepository {
    private final Firestore db;

    // 전체 시설 조회
    public List<Facility> findAllFacilities() throws Exception {
        List<Facility> result = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("facilities").get();
        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            result.add(doc.toObject(Facility.class));
        }
        return result;
    }

    // 단일 시설 조회
    public Facility findById(String id) throws Exception {
        DocumentSnapshot doc = db.collection("facilities").document(id).get().get();
        return doc.exists() ? doc.toObject(Facility.class) : null;
    }

    // currentCount, 혼잡도 업데이트
    public void updateFacilityAsync(FacilityUpdateRequest updateRequest) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("currentCount", updateRequest.getCurrentCount());
        updates.put("congestionLevel", updateRequest.getCongestionLevel());
        updates.put("updatedAt", updateRequest.getUpdatedAt());

        ApiFuture<WriteResult> future = db.collection("facilities")
                .document(updateRequest.getId())
                .update(updates);

        future.addListener(() -> {
            try {
                System.out.println("Updated: " + future.get().getUpdateTime());
            } catch (Exception e) {
                System.err.println("Firestore update failed: " + e.getMessage());
            }
        }, Runnable::run);
    }
}
