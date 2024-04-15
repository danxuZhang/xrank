package xrank;

public class TextRankSummarizer implements Summarizer{
    private final TextRankRpcClient client;
    public TextRankSummarizer() {
        client = new TextRankRpcClient(Constants.grpcHost, Constants.grpcPort);
    }

    @Override
    public String summarize(String inputText) {
        return client.summarize(inputText);
    }
}
