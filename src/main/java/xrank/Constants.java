package xrank;

import java.util.Arrays;
import java.util.HashSet;

class Constants {
    public static final String kafkaBroker = "localhost:9093";
    public static final String kafkaInputTopic = "input";
    public static final String kafkaDefaultOutputTopic = "output";
    public static final HashSet<String> kafkaOutputTopics = new HashSet<>(Arrays.asList(
            "Blockchain",
            "Semiconductor",
            "Biotechnology",
            "Artificial Intelligence",
            "Vehicle"
    ));

    public static final String grpcHost = "localhost";
    public static final int grpcPort = 50051;

    public static final int flinkSlidingWindowSize = 30;
    public static final int flinkSlidingWindowGap = 5;
}