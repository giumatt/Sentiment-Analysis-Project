import SentimentAnalysis.Services.AudioCapture;
import SentimentAnalysis.Services.SpeechToText;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class TestSpeechToText {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        String modelPath = "/home/mattia/Documents/vosk-model-en-us-daanzu-20200905";
        SpeechToText speechToText = new SpeechToText(modelPath);

        AudioCapture audioCapture = new AudioCapture();
        audioCapture.start();

        byte[] buffer = new byte[4096];

        System.out.println("Listening from microphone. Speak now...");

        while (true) {
            int bytesRead = audioCapture.read(buffer);
            if (bytesRead > 0) {

                String recognizedText = speechToText.processAudio(buffer);
                if (recognizedText != null && !recognizedText.trim().isEmpty()) {
                    System.out.println("Recognized: " + recognizedText);
                }
            } else {

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrotto: " + e.getMessage());
                }
            }
        }
    }
}
