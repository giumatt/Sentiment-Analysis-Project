import org.vosk.*;

import java.io.IOException;

public class SpeechToText {
    private final Recognizer recognizer;

    public SpeechToText(String modelPath) throws IOException {
        Model model = new Model(modelPath);
        recognizer = new Recognizer(model, 16000);
    }

    public String processAudio(byte[] audioBuffer) {
        if (recognizer.acceptWaveForm(audioBuffer, audioBuffer.length)) {
            return extractTextFromResult(recognizer.getResult());
        }
        return null;
    }

    private String extractTextFromResult(String result) {
        if (result.contains("\"text\"")) {
            int start = result.indexOf("\"text\":\"") + 8;
            int end = result.indexOf("\"", start);
            return result.substring(start, end);
        }
        return "";
    }
}
