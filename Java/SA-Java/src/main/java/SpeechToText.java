import org.vosk.*;
import java.io.IOException;
import org.json.JSONObject;

public class SpeechToText {
    private final Recognizer recognizer;

    public SpeechToText(String modelPath) throws IOException {
        Model model = new Model(modelPath);
        recognizer = new Recognizer(model, 16000.0f);
    }

    public void writeResult(byte[] buffer) {
        if (recognizer.acceptWaveForm(buffer, buffer.length)) {
            System.out.println("Result: " + recognizer.getResult());
        } else {
            System.out.println("Partial: " + recognizer.getPartialResult());
        }
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
