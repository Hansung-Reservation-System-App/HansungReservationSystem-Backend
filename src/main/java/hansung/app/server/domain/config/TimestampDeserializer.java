package hansung.app.server.domain.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.Timestamp;

import java.io.IOException;
//JSON → Timestamp 객체로 변환하는 방법을 Spring에게 알려주는 클래스
public class TimestampDeserializer extends JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p); // JSON을 트리 형태(JsonNode) 로 읽음
        long seconds = node.get("seconds").asLong();
        int nanos = node.get("nanos").asInt();

        return Timestamp.ofTimeSecondsAndNanos(seconds, nanos); // Google Cloud Timestamp 생성 메서드 사용
    }
}
