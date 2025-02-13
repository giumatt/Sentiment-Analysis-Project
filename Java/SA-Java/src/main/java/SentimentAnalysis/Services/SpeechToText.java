package SentimentAnalysis.Services;

import SentimentAnalysis.Interfaces.SpeechToTextInterface;
import org.vosk.*;
import java.io.IOException;
import org.json.JSONObject;

public class SpeechToText implements SpeechToTextInterface {
    private final Recognizer recognizer;

    public SpeechToText(String modelPath) throws IOException {
        Model model = new Model(modelPath);
        recognizer = new Recognizer(model, 16000.0f);
    }

    public String processAudio(byte[] audioBuffer) {
        if (recognizer.acceptWaveForm(audioBuffer, audioBuffer.length)) {
            return extractTextFromResult(recognizer.getResult());
        }
        return null;
    }

    private String extractTextFromResult(String result) {
        try {
            JSONObject json = new JSONObject(result);
            return json.optString("text", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}