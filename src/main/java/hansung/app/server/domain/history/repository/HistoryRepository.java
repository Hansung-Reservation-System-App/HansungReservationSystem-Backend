package hansung.app.server.domain.history.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import hansung.app.server.domain.history.entity.History;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepository {

    private final Firestore db;

    // 기록 저장
    public ApiFuture<WriteResult> save(History history) {
        return db.collection("history")
                .document(history.getId())
                .set(history);
    }

    // 시설 ID와 날짜에 따른 기록 조회
    public List<History> findHistoryByFacilityIdAndDate(
            String facilityId, Timestamp start, Timestamp end) throws Exception {

        List<History> result = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = db.collection("history")
                .whereEqualTo("facilityId", facilityId)
                .whereGreaterThanOrEqualTo("date", start)
                .whereLessThan("date", end)
                .get();

        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            result.add(doc.toObject(History.class));
        }
        return result;
    }

    // 시설 ID로 모든 기록 조회
    public List<History> findAllHistoryByFacilityId(String facilityId) throws Exception {
        List<History> result = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("history")
                .whereEqualTo("facilityId", facilityId)
                .get();
        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            result.add(doc.toObject(History.class));
        }
        return result;
    }
}

