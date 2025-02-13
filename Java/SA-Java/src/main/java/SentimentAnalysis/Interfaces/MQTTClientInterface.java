package SentimentAnalysis.Interfaces;

public interface MQTTClientInterface {
    void publish(String message) throws Exception;

    void disconnect() throws Exception;
}
