package SentimentAnalysis.Interfaces;

public interface AudioCaptureInterface {
    void start();

    int read(byte[] buffer);

    void stop();
}
