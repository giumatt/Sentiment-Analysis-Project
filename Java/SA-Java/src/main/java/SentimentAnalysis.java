import edu.stanford.nlp.pipeline.*;

import java.util.Properties;

public class SentimentAnalysis {
    private final StanfordCoreNLP pipeline;

    public SentimentAnalysis() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public String analyzeSentiment(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        for (CoreSentence sentence : document.sentences()) {
            return sentence.sentiment();
        }
        return "Neutral";
    }
}
