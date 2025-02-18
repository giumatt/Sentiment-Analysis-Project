package SentimentAnalysis.Services;

import SentimentAnalysis.Config.ConfigLoader;
import SentimentAnalysis.Interfaces.SentimentAnalysisInterface;
import edu.stanford.nlp.pipeline.*;
import java.util.Properties;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

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
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiToken); // Inserisci il tuo token
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"inputs\": \"" + text + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.length() > 0) {
                JSONArray innerArray = jsonArray.getJSONArray(0);
                if(innerArray.length() > 0) {
                    JSONObject sentimentObject = innerArray.getJSONObject(0);
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
