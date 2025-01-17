import javax.sound.sampled.*;

public class AudioCapture {
    private TargetDataLine microphone;

    public AudioCapture() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        this.microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
    }

    public void start() { microphone.start(); }

    public byte[] captureAudio() {
        byte[] buffer = new byte[4096];
        microphone.read(buffer, 0, buffer.length);
        return buffer;
    }

    public void stop() { microphone.stop(); }
}
