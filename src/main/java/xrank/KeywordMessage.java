package xrank;

public class KeywordMessage {
    private String keyword;
    private String content;

    public KeywordMessage() {}

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "KeywordMessage{" +
                "keyword='" + keyword + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
