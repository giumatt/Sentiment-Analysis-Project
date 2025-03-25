package SentimentAnalysis.Services;

import SentimentAnalysis.Config.ConfigLoader;
import SentimentAnalysis.Interfaces.SentimentAnalysisInterface;
import edu.stanford.nlp.pipeline.*;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.URI;

public class SentimentAnalysis implements SentimentAnalysisInterface {
    private final StanfordCoreNLP pipeline;
    private boolean useAPI;
    private String apiURL = ConfigLoader.getProperty("apiURL");
    private String apiToken = ConfigLoader.getProperty("apiToken");

    public SentimentAnalysis(boolean useAPI) {
        this.useAPI = useAPI;

        if(!useAPI) {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
            pipeline = new StanfordCoreNLP(props);
        } else {
            pipeline = null;
        }
    }

    public String analyzeSentiment(String text) {
        if(useAPI) {
            return analyzeSentimentIta(text);
        } else {
            return analyzeSentimentEng(text);
        }
    }

    private String analyzeSentimentEng(String text) {
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        if(!document.sentences().isEmpty()) {
            CoreSentence sentence = document.sentences().getFirst();
            return sentence.sentiment();
        }

        return "Neutral";
    }

    private String analyzeSentimentIta(String text) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String jsonInputString = "{\"inputs\": \"" + text + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiURL))
                    .header("Authorization", "Bearer " + apiToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray responseArray = new JSONArray(response.body());

            if (!responseArray.isEmpty()) {
                JSONArray sentimentArray = responseArray.getJSONArray(0);
                if (!sentimentArray.isEmpty()) {
                    JSONObject sentimentObject = sentimentArray.getJSONObject(0);
                    return sentimentObject.getString("label");
                }
            }

            return "Neutral";
        } catch (Exception e) {
            e.printStackTrace();
            return "Neutral";
        }
    }
}
