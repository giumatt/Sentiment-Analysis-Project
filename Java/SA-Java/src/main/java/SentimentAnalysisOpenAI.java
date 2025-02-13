import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class SentimentAnalysisOpenAI {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = ConfigLoader.getProperty("OPENAI_API_KEY");

    public static String analyzeSentimentOAI(String text) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String prompt = "Analizza il sentiment del seguente testo e restituisci solo una di queste parole: Positive, Neutral, Negative.\nTesto: \"" + text + "\"";

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("temperature", 0.0);
        requestBody.put("max_tokens", 10);

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", "Sei un assistente AI che esegue Sentiment Analysis in italiano."));
        messages.put(new JSONObject().put("role", "user").put("content", prompt));

        requestBody.put("messages", messages);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Error during request: " + response);
            }

            JSONObject jsonResponse = new JSONObject(response.body().string());

            return jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        }
    }
}
