import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        AudioCapture audio = new AudioCapture();
        SpeechToText speech = new SpeechToText("/home/mattia/Downloads/vosk-model-it-0.22");
        //TextSegmentation segmentation = new TextSegmentation();
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
}