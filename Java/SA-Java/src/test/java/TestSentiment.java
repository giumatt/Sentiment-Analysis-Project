public class TestSentiment {
    public static void main(String[] args) {
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();

        String[] testSentences = {
                "I love this museum!",
                "This museum is awesome!",
                "I don't really like this place.",
                "The visit was very pleasant.",
                "The exhibition is disappointing.",
                "I don't like nor dislike this exhibition."
        };

        for (String sentence : testSentences) {
            String result = sentimentAnalysis.analyzeSentiment(sentence);
            System.out.println("Phrase: " + sentence);
            System.out.println("Sentiment: " + result);
            System.out.println("------------------------------");
        }
    }
}
