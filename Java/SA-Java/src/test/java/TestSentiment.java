import SentimentAnalysis.Services.SentimentAnalysis;

public class TestSentiment {
    public static void main(String[] args) {
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(true);

        String[] testSentences = {
                "Amo questo museo!",
                "Questo museo è fantastico!",
                "Questo posto non mi piace molto.",
                "La visita è veramente piacevole.",
                "Questa mostra è insoddisfacente.",
                "Non so se mi piace."
        };

        for (String sentence : testSentences) {
            String result = sentimentAnalysis.analyzeSentiment(sentence);
            System.out.println("Phrase: " + sentence);
            System.out.println("Sentiment: " + result);
            System.out.println("------------------------------");
        }
    }
}
