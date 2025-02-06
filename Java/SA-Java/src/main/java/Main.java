import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int PAUSE_THRESHOLD = 2000;
    private static final int MIN_WORDS_THRESHOLD = 5;
    private static final String NODE_RED_URL = "http://localhost:1880/sentiment";

    public static void main(String[] args) throws LineUnavailableException, IOException {
        AudioCapture audio = new AudioCapture();
        SpeechToText speech = new SpeechToText("/home/mattia/Documents/vosk-model-en-us-0.22");
        SentimentAnalysis sentiment = new SentimentAnalysis();
        DataStorage storage = new DataStorage();

        // Part for defining sentence length and pauses between sentences
        StringBuilder phrase = new StringBuilder();
        long lastSpeechTimestamp = System.currentTimeMillis();

        audio.start();

        LOGGER.info("Listening for audio...");

        byte[] buffer = new byte[4096];
        
        while(true) {
            int bytesRead = audio.read(buffer);
            
            if(bytesRead > 0) {
                String recognizedText = speech.processAudio(buffer);

                if (recognizedText != null && !recognizedText.isEmpty()) {
                    lastSpeechTimestamp = System.currentTimeMillis();

                    phrase.append(recognizedText).append(" ");

                    String[] words = phrase.toString().trim().split("\\s+");

                    if (words.length > MIN_WORDS_THRESHOLD) {
                        String sentimentLabel = sentiment.analyzeSentiment(phrase.toString().trim());

                        //storage.saveSentence(phrase.toString().trim(), sentimentLabel);
                        sendToNodeRedAsync(phrase.toString().trim(), sentimentLabel);

                        //System.out.println("Saved: " + phrase + "with sentiment: " + sentimentLabel);
                        LOGGER.info("Sent to Node-RED: " + phrase + " | Sentiment: " + sentimentLabel);
                        phrase.setLength(0);
                    }
                }

                // Check if there's an extended pause
                if (System.currentTimeMillis() - lastSpeechTimestamp > PAUSE_THRESHOLD && !phrase.isEmpty()) {
                    String sentimentLabel = sentiment.analyzeSentiment(phrase.toString().trim());

                    //storage.saveSentence(phrase.toString().trim(), sentimentLabel);
                    sendToNodeRedAsync(phrase.toString().trim(), sentimentLabel);
                    //System.out.println("Saved: " + phrase + "with sentiment: " + sentimentLabel);
                    LOGGER.info("Sent to Node-RED: " + phrase + " | Sentiment: " + sentimentLabel);
                    phrase.setLength(0);
                }
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Interrupped thread: " + e.getMessage());
                }
            }
        }
    }

    private static void sendToNodeRed(String text, String sentiment) {
        HttpURLConnection conn = null;
        try {
            URI uri = new URI(NODE_RED_URL);
            URL url = uri.toURL();       // Node-RED endpoint
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"text\": \"" + text + "\", \"sentiment\": \"" + sentiment + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                LOGGER.severe("Error during Node-RED request. Response code: " + responseCode);
            }
        } catch (IOException | URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static void sendToNodeRedAsync(String text, String sentiment) {
        new Thread(() -> sendToNodeRed(text, sentiment)).start();
    }
}