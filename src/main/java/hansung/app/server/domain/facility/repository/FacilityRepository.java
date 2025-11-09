package hansung.app.server.domain.facility.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import hansung.app.server.domain.facility.entity.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FacilityRepository {
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
    public void updateFacility(Facility facility) {
        db.collection("facilities").document(facility.getId()).set(facility);
    }
}
