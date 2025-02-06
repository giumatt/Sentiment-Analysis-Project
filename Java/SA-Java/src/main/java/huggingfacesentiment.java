import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class huggingfacesentiment {
    public static void main(String[] args) throws Exception {
        String apiToken = "";
        String apiUrl = "https://api-inference.huggingface.co/models/tabularisai/multilingual-sentiment-analysis";
        String inputText = "{ \"inputs\": \"Questa giornata è veramente brutta, sono indignato.\" }";


        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Scrivi il testo da analizzare
        OutputStream os = connection.getOutputStream();
        os.write(inputText.getBytes());
        os.flush();
        os.close();

        // Leggi la risposta
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        br.close();

        // Analizza la risposta JSON
        JSONArray outerArray = new JSONArray(response.toString()); // Primo livello dell'array
        JSONArray jsonArray = outerArray.getJSONArray(0); // Estrai il secondo livello dell'array

        // Trova il sentimento con lo score più alto
        String bestSentiment = "";
        double bestScore = 0.0;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String sentiment = obj.getString("label");
            double score = obj.getDouble("score");

            if (score > bestScore) {
                bestScore = score;
                bestSentiment = sentiment;
            }
        }

        // Stampa solo il sentimento con il punteggio più alto
        System.out.println("Sentimento predominante: " + bestSentiment + " (score: " + bestScore + ")");
    }
}