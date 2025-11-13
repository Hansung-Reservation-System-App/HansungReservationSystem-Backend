package hansung.app.server.domain.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/hansung-reservation-system-firebase-adminsdk-fbsvc-6db401b62e.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("🔥 FirebaseApp has been initialized successfully!");
        }
    }

    @Bean
    public Firestore firestore() {
        Firestore db = FirestoreClient.getFirestore();
        System.out.println("✅ Firestore connected: " + db.getOptions().getProjectId());
        return db;
    }
}