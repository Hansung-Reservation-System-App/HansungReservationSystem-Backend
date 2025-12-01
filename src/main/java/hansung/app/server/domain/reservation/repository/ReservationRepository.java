package hansung.app.server.domain.reservation.repository;


import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import hansung.app.server.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final Firestore db;

    // 예약 ID로 조회
    public Reservation findById(String reservationId) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = db.collection("reservations").document(reservationId).get().get();
        return doc.exists() ? doc.toObject(Reservation.class) : null;
    }

    // 예약 저장
    public ApiFuture<WriteResult> save(Reservation reservation) {
        DocumentReference reservationRef = db.collection("reservations").document(reservation.getId());
        return reservationRef.set(reservation);
    }

    // 예약 삭제
    public ApiFuture<WriteResult> delete(String reservationId) {
        DocumentReference reservationRef = db.collection("reservations").document(reservationId);
        return reservationRef.delete();
    }

    // 예약 상태 변경 (취소 등)
    public ApiFuture<WriteResult> updateStatus(String reservationId, String status) {
        DocumentReference reservationRef = db.collection("reservations").document(reservationId);
        return reservationRef.update("status", status);
    }

    // 시설 ID로 예약된 모든 예약 정보 조회
    public List<Reservation> findReservationsByFacilityId(String facilityId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection("reservations")
                .whereEqualTo("facilityId", facilityId)
                .get();

        List<Reservation> reservations = new ArrayList<>();

        // Firestore에서 반환된 문서들을 Reservation 객체로 변환하여 리스트에 추가
        for (DocumentSnapshot document : future.get().getDocuments()) {
            reservations.add(document.toObject(Reservation.class));
        }

        return reservations;
    }

    //예약 연장
    public ApiFuture<WriteResult> updateEndTime(String reservationId, Timestamp newEndTime) {
        DocumentReference reservationRef = db.collection("reservations").document(reservationId);
        return reservationRef.update("endTime", newEndTime);  // 종료 시간 업데이트
    }

    //사용자 ID로 예약 정보 조회
    public List<Reservation> findByUserId(String userId) throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection("reservations")
                .whereEqualTo("userId", userId)
                .get();

        List<Reservation> result = new ArrayList<>();
        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            result.add(doc.toObject(Reservation.class));
        }
        return result;
    }

    // 시간 만료된 예약들 자동 취소
    public void autoCancelExpiredReservations(Timestamp now) throws Exception {
        // endTime < now 이면서 status == "진행 중" 인 예약만 조회
        ApiFuture<QuerySnapshot> future = db.collection("reservations")
                .whereLessThan("endTime", now)
                .whereEqualTo("status", "진행 중")
                .get();

        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        if (docs.isEmpty()) {
            return; // 완료할 예약 없음
        }

        WriteBatch batch = db.batch();

        for (QueryDocumentSnapshot doc : docs) {
            DocumentReference ref = doc.getReference();
            // status만 "완료"로, isActive는 false로
            batch.update(ref,
                    "status", "완료",
                    "isActive", false
            );
        }

        // 배치 커밋 (동기적으로 기다림)
        batch.commit().get();
    }

}

