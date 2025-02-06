import edu.stanford.nlp.pipeline.*;

import java.util.Properties;

public class SentimentAnalysis {
    private final StanfordCoreNLP pipeline;

    public SentimentAnalysis() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }
    /* VERSION 1.0
    public String analyzeSentiment(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        for (CoreSentence sentence : document.sentences()) {
            return sentence.sentiment();
        }
        return "Neutral";
    }
    */

    // VERSION 2.0
    public String analyzeSentiment(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        if(!document.sentences().isEmpty()) {
            CoreSentence sentence = document.sentences().getFirst();
            return sentence.sentiment();
        }

        return "Neutral";
    }
}
