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
            if(bytesRead > 0) speech.writeResult(buffer);
        }

//        while (true) {
//            if(byte[] audioBuffer = audio.captureAudio();)
//            String text = speech.processAudio(audioBuffer);
//
//            if (text != null && !text.isEmpty()) {
//                System.out.println("Recognized text: " + text);
////                if (segmentation.isEndOfSentence(text)) {
////                    String sentimentPartial = sentiment.analyzeSentiment(text);
////                    storage.saveSentence(text, sentimentPartial);
////                    System.out.println("Saved -> " + text + " : " + sentimentPartial);
//                }
//            }
//        }
    }
}
