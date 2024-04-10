package xrank;

public class DummySummarizer implements Summarizer{

    @Override
    public String summarize(String input) {
        return "$" + input;
    }
}
