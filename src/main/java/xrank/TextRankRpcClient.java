package xrank;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import textrank.TextRankServiceGrpc;
import textrank.Textrank.Message;

public class TextRankRpcClient {
    private final TextRankServiceGrpc.TextRankServiceBlockingStub blockingStub;

    public TextRankRpcClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = TextRankServiceGrpc.newBlockingStub(channel);
    }

    public String summarize(String inputText) {
        Message request = Message.newBuilder().setText(inputText).build();
        Message response = blockingStub.summarize(request);
        return response.getText();
    }

    public static void main(String[] args) {
        TextRankRpcClient client = new TextRankRpcClient("localhost", 8980);
        String response = client.summarize("Your input text here");
        System.out.println("Summary: " + response);
    }
}
