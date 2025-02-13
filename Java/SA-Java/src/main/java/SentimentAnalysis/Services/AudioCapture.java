package SentimentAnalysis.Services;

import SentimentAnalysis.Interfaces.AudioCaptureInterface;

import javax.sound.sampled.*;

public class AudioCapture implements AudioCaptureInterface {
    private TargetDataLine microphone;

    public AudioCapture() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        this.microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
    }

    public void start() { microphone.start(); }

    public int read(byte[] buffer) {
        return microphone.read(buffer, 0, buffer.length);
    }

//    public int captureAudio() {
//        byte[] buffer = new byte[4096];
//        return microphone.read(buffer, 0, buffer.length);
//    }

    public void stop() { microphone.stop(); }
}
