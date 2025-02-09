import java.util.*;

public class ItalianSentimentAnalysis extends SentimentAnalysis {
    private final Map<String, Integer> sentimentWords;
    private final Map<String, Integer> sentimentPhrases;

    public ItalianSentimentAnalysis() {
        sentimentWords = new HashMap<>();
        sentimentPhrases = new HashMap<>();

        addPositiveWords(new String[]{
                "bello", "bellissimo", "meraviglioso", "straordinario", "affascinante", "emozionante",
                "magnifico", "incantevole", "splendido", "stupendo", "fantastico", "mozzafiato",
                "entusiasmante", "eccezionale", "grandioso", "ispiratore", "divertente", "elegante"
        }, 2);

        addPositiveWords(new String[]{
                "buono", "interessante", "piacevole", "apprezzabile", "godibile", "ben fatto",
                "istruttivo", "educativo", "chic", "equilibrato", "ben concepito"
        }, 1);

        addNegativeWords(new String[]{
                "brutto", "orribile", "deludente", "pessimo", "banale", "insignificante",
                "mediocre", "confuso", "ripetitivo", "trascurato", "sciatto", "prevedibile",
                "freddo", "scialbo", "sopravvalutato", "inutile", "fastidioso", "ridicolo",
                "malriuscito", "forzato", "sterile", "privo di anima", "senza emozione",
                "faticoso", "monotono", "incomprensibile", "noioso"
        }, -2);

        addNegativeWords(new String[]{
                "poco ispirato", "poco interessante", "debole", "delusione", "disorganizzato",
                "anonimo", "blando", "non mi è piaciuto", "troppo caro"
        }, -1);

        addPhrase("esperienza incredibile", 3);
        addPhrase("mostra ben organizzata", 2);
        addPhrase("atmosfera rilassante", 2);
        addPhrase("vale la pena", 2);
        addPhrase("consigliato a tutti", 3);
        addPhrase("un capolavoro", 3);
        addPhrase("mi ha emozionato", 3);
        addPhrase("troppo affollato", -2);
        addPhrase("non vale il prezzo", -3);
        addPhrase("mi aspettavo di più", -2);
        addPhrase("troppo costoso", -2);
        addPhrase("personale scortese", -3);
        addPhrase("organizzazione pessima", -3);
        addPhrase("non ben segnalato", -2);
    }

    private void addPositiveWords(String[] words, int score) {
        for (String word : words) {
            sentimentWords.put(word, score);
        }
    }

    private void addNegativeWords(String[] words, int score) {
        for (String word : words) {
            sentimentWords.put(word, score);
        }
    }

    private void addPhrase(String phrase, int score) {
        sentimentPhrases.put(phrase.toLowerCase(), score);
    }

    @Override
    public String analyzeSentiment(String text) {
        int sentimentScore = 0;
        text = text.toLowerCase();

        if (text.contains("non") || text.contains("mai") || text.contains("nemmeno")) {
            sentimentScore -= 1;
        }
        for (Map.Entry<String, Integer> entry : sentimentPhrases.entrySet()) {
            if (text.contains(entry.getKey())) {
                sentimentScore += entry.getValue();
            }
        }

        String[] words = text.split("\\s+");
        for (String word : words) {
            if (sentimentWords.containsKey(word)) {
                sentimentScore += sentimentWords.get(word);
            }
        }

        if (sentimentScore > 2) {
            return "Very positive";
        } else if (sentimentScore > 0) {
            return "Positive";
        } else if (sentimentScore < -2) {
            return "Very negative";
        } else if (sentimentScore < 0) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }

}
