import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        AudioCapture audio = new AudioCapture();
        SpeechToText speech = new SpeechToText("/home/mattia/Downloads/vosk-model-it-0.22");
        TextSegmentation segmentation = new TextSegmentation();
        SentimentAnalysis sentiment = new SentimentAnalysis();
        DataStorage storage = new DataStorage();

        audio.start();

        System.out.println("Listening for audio...");

        byte[] buffer = new byte[4096];
        
        while(true) {
            int bytesRead = audio.read(buffer);
            
            if(bytesRead > 0) {
              String recognizedText = speech.processAudio(buffer);

              if(recognizedText != null && !recognizedText.isEmpty()) {
                System.out.println("Recognized text: " + recognizedText);

                if(segmentation.isEndOfSentence(recognizedText)) {
                  String sentimentLabel = sentiment.analyzeSentiment(recognizedText);

                  storage.saveSentence(recognizedText, sentimentLabel);

                  System.out.println("Saved: " + recognizedText + "with sentiment: " + sentimentLabel);
                }
              }
            }
        }
    }
}