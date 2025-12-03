package hansung.app.server.domain.facility.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import hansung.app.server.domain.facility.dto.request.FacilityUpdateRequest;
import hansung.app.server.domain.facility.entity.Facility;
import hansung.app.server.domain.facility.exception.FacilityException;
import hansung.app.server.domain.facility.exception.code.FacilityErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
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
        ApiFuture<QuerySnapshot> future = db.collection("facilities")
                .whereEqualTo("id", id)
                .get();

        QuerySnapshot querySnapshot = future.get();
        if (!querySnapshot.isEmpty()) {
            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            return document.toObject(Facility.class);
        }
        return null;  // 문서가 없으면 null 반환
    }

    // 센서 ID로 시설 조회
    public Facility findBySensorId(String sensorId) throws Exception {
        log.debug("Looking for facility with sensorId: {}", sensorId);  // sensorId 확인 로그 추가

        // Firestore에서 sensorId에 맞는 시설 찾기
        ApiFuture<QuerySnapshot> future = db.collection("facilities")
                .whereEqualTo("sensorId", sensorId)
                .get();

        QuerySnapshot querySnapshot = future.get();  // 쿼리 결과를 가져옴

        // 쿼리 결과가 있는지 확인
        if (!querySnapshot.isEmpty()) {
            // 첫 번째 문서 가져오기
            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            log.debug("Found document: {}", document.getId());  // 문서 ID 출력
            log.debug("Document fields: {}", document.getData());  // 문서 내용 출력

            // 문서 객체로 변환
            return document.toObject(Facility.class);
        }

        // 문서가 없으면 null 반환
        return null;
    }


    // currentCount, 혼잡도 업데이트
    public void updateFacility(FacilityUpdateRequest request) throws Exception {
        // 트랜잭션을 사용하여 Firestore에서 문서 업데이트
        Transaction.Function<WriteResult> updateFunction = transaction -> {
            ApiFuture<QuerySnapshot> future = db.collection("facilities")
                    .whereEqualTo("id", request.getId())  // 'id' 필드 값으로 문서 조회
                    .get();

            QuerySnapshot querySnapshot = future.get();
            if (querySnapshot.isEmpty()) {
                log.error("Facility document not found with id field: {}", request.getId());
                throw new FacilityException(FacilityErrorCode.DOCUMENT_NOT_FOUND);
            }

            // 첫 번째 문서 참조
            DocumentSnapshot snapshot = querySnapshot.getDocuments().get(0);
            DocumentReference docRef = snapshot.getReference();

            log.debug("Transaction started for document: {}", docRef.getId());

            // 업데이트할 데이터 설정
            log.debug("Updating fields: currentCount = {}, congestionLevel = {}", request.getCurrentCount(), request.getCongestionLevel());
            Map<String, Object> updates = new HashMap<>();
            updates.put("currentCount", request.getCurrentCount());  // currentCount만 업데이트
            updates.put("congestionLevel", request.getCongestionLevel());  // 혼잡도도 업데이트
            updates.put("updatedAt", request.getUpdatedAt());  // 추가로 업데이트할 데이터

            // 트랜잭션 내에서 문서 업데이트
            transaction.update(docRef, updates);

            return null;
        };

        try {
            db.runTransaction(updateFunction).get();  // 트랜잭션 실행
        } catch (Exception e) {
            log.error("Firestore update failed for facility: {}", request.getId(), e);
            throw new FacilityException(FacilityErrorCode.FIRESTORE_UPDATE_FAILED);  // 에러 코드 업데이트
        }
    }

    //예약 생성 시 currentCount +1
    public ApiFuture<WriteResult> increaseCurrentCount(String facilityId) throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection("facilities")
                .whereEqualTo("id", facilityId)
                .limit(1)
                .get();

        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        if (docs.isEmpty()) {
            throw new FacilityException(FacilityErrorCode.FACILITY_NOT_FOUND);
        }

        // 2) 찾은 문서에 대해 currentCount +1
        DocumentReference ref = docs.get(0).getReference();
        return ref.update("currentCount", FieldValue.increment(1));
    }

    //예약 취소 / 종료 시 currentCount -1
    public ApiFuture<WriteResult> decreaseCurrentCount(String facilityId) throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection("facilities")
                .whereEqualTo("id", facilityId)
                .limit(1)
                .get();

        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        if (docs.isEmpty()) {
            throw new FacilityException(FacilityErrorCode.FACILITY_NOT_FOUND);
        }

        DocumentReference ref = docs.get(0).getReference();
        return ref.update("currentCount", FieldValue.increment(-1));
    }
}

