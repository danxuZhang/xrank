package xrank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;

public class Main {
    public static final String[] inputTopics = {"nvidia", "tesla"};
    public static final String outputTopic = "output";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final String brokers = "localhost:9093";

        for (String topic : inputTopics) {
            KafkaSource<String> source = KafkaSource.<String>builder()
                    .setBootstrapServers(brokers)
                    .setTopics(topic)
                    .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(StringDeserializer.class))
                    .build();

            DataStream<String> stream = env.fromSource(source, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(2
            )), "Kafka Source");

            DataStream<String> summarizedStream = stream
                    .windowAll(SlidingEventTimeWindows.of(Time.seconds(30), Time.seconds(5)))
                    // .trigger(new FiveSecondIntervalTrigger())
                    .apply(new SummarizeWindowFunction());


            KafkaSink<String> sink = KafkaSink.<String>builder()
                    .setBootstrapServers(brokers)
                    .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                            .setTopic(outputTopic)
                            .setValueSerializationSchema(new SimpleStringSchema())
                            .build()
                    )
                    .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                    .build();

            summarizedStream.print();
            summarizedStream.sinkTo(sink);
        }


        env.execute("Kafka Flink Summarization");
    }

    public static class SummarizeWindowFunction implements AllWindowFunction<String, String, TimeWindow> {
        private static final long serialVersionUID = 1L; // Explicit serialVersionUID

        @Override
        public void apply(TimeWindow window, Iterable<String> values, Collector<String> out) throws Exception {
            Summarizer summarizer = new DummySummarizer(); // Assuming Summarizer is serializable
            StringBuilder sb = new StringBuilder();
            for (String value : values) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    JsonNode rootNode = mapper.readTree(value);
                    String content = rootNode.path("content").asText();
                    System.out.println("Extracted content: " + content);
                    sb.append(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String summarized = summarizer.summarize(sb.toString());
            out.collect(summarized);
        }
    }
}
