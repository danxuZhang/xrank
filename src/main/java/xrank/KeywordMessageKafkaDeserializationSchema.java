package xrank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class KeywordMessageKafkaDeserializationSchema implements KafkaRecordDeserializationSchema<KeywordMessage> {

    private static final long serialVersionUID = 1L;
    private transient ObjectMapper mapper;

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<KeywordMessage> out) throws IOException {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        // Assuming the message is in the value part of the Kafka record.
        byte[] message = record.value();
        if (message != null) {
            JsonNode jsonNode = mapper.readTree(message);
            KeywordMessage km = new KeywordMessage();
            km.setKeyword(jsonNode.get("keyword").asText());
            km.setContent(jsonNode.get("content").asText());
            out.collect(km);
        }
    }

    @Override
    public TypeInformation<KeywordMessage> getProducedType() {
        return TypeExtractor.getForClass(KeywordMessage.class);
    }
}
