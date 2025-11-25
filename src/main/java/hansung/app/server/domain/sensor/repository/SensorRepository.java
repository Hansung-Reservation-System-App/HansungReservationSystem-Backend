package hansung.app.server.domain.sensor.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import hansung.app.server.domain.sensor.entity.Sensor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SensorRepository {

    private final Firestore db;

    public ApiFuture<WriteResult> save(Sensor sensor) {
        return db.collection("sensorLogs")
                .document(sensor.getId())
                .set(sensor);
    }
}
