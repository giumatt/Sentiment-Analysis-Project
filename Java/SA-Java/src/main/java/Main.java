import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        AudioCapture audio = new AudioCapture();
        SpeechToText speech = new SpeechToText("/home/mattia/Downloads/vosk-model-it-0.22");
        SentimentAnalysis sentiment = new SentimentAnalysis();
        DataStorage storage = new DataStorage();

        // Part for defining sentence length and pauses between sentences
        StringBuilder phrase = new StringBuilder();
        long lastSpeechTimestamp = System.currentTimeMillis();
        final int pauseThreshold = 2000;
        final int minWordsThreshold = 5;

        audio.start();

        System.out.println("Listening for audio...");

        byte[] buffer = new byte[4096];
        
        while(true) {
            int bytesRead = audio.read(buffer);
            
            if(bytesRead > 0) {
              String recognizedText = speech.processAudio(buffer);

              if(recognizedText != null && !recognizedText.isEmpty()) {
                lastSpeechTimestamp = System.currentTimeMillis();

                phrase.append(recognizedText).append(" ");

                String[] words = phrase.toString().trim().split("\\s+");

                if(words.length > minWordsThreshold) {
                  String sentimentLabel = sentiment.analyzeSentiment(phrase.toString().trim());

                  storage.saveSentence(phrase.toString().trim(), sentimentLabel);

                  System.out.println("Saved: " + phrase + "with sentiment: " + sentimentLabel);

                  phrase.setLength(0);
                }
              }

              // Check if there's an extended pause
              if (System.currentTimeMillis() - lastSpeechTimestamp > pauseThreshold && phrase.length() > 0) {
                String sentimentLabel = sentiment.analyzeSentiment(phrase.toString().trim());

                storage.saveSentence(phrase.toString().trim(), sentimentLabel);

                System.out.println("Saved: " + phrase + "with sentiment: " + sentimentLabel);

                phrase.setLength(0);
              } 
            }
        }
    }

    private static void sendToNodeRed(String text, String sentiment) {
        try {
            URI uri = new URI("http://localhost:1800/sentiment");
            URL url = uri.toURL();       // Node-RED endpoint
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"text\": \"" + text + "\", \"sentiment\": \"" + sentiment + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            conn.getResponseCode();
        } catch (IOException e) { e.printStackTrace(); } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}