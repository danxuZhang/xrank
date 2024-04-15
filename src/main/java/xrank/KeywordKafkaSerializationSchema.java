package xrank;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;

public class KeywordKafkaSerializationSchema implements KafkaRecordSerializationSchema<String> {

    private final String defaultTopic;

    public KeywordKafkaSerializationSchema(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(String element, KafkaSinkContext context, Long timestamp) {
        String[] parts = element.split(",", 2);  // Assuming the keyword and data are comma-separated
        String keyword = parts[0].trim();  // Extract the keyword
        String data = parts.length > 1 ? parts[1].trim() : "";

        // Use keyword to determine the topic
        String topicName = keyword;  // This is a simplistic mapping, adapt as necessary
        if (!Constants.kafkaOutputTopics.contains(topicName)) {
            topicName = defaultTopic;
        }

        return new ProducerRecord<>(topicName, data.getBytes(StandardCharsets.UTF_8));
    }
}
