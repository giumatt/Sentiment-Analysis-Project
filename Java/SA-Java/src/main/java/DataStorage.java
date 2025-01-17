import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private final List<SentenceData> sentences;

    public DataStorage() {
        sentences = new ArrayList<>();
    }

    public void saveSentence(String text, String sentiment) {
        sentences.add(new SentenceData(text, sentiment));
    }

    public List<SentenceData> getAllSentences() {
        return sentences;
    }

    static class SentenceData {
        String text;
        String sentiment;

        public SentenceData(String text, String sentiment) {
            this.text = text;
            this.sentiment = sentiment;
        }

        @Override
        public String toString() {
            return "Text: " + text + " | Sentiment: " + sentiment;
        }
    }
}
