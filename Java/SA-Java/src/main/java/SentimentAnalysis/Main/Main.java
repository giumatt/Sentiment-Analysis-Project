package SentimentAnalysis.Main;

import SentimentAnalysis.Config.ConfigLoader;
import SentimentAnalysis.Services.AudioCapture;
import SentimentAnalysis.Services.MQTTClient;
import SentimentAnalysis.Services.SpeechToText;
import SentimentAnalysis.Services.SentimentAnalysis;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int PAUSE_THRESHOLD = 2000;
    private static final int MIN_WORDS_THRESHOLD = 3;
    private static MQTTClient mqttClient;

    public static void main(String[] args) throws LineUnavailableException, IOException, MqttException {
        String modelPathIT = ConfigLoader.getProperty("vosk.model.it");
        String modelPathEN = ConfigLoader.getProperty("vosk.model.en");

        String broker = ConfigLoader.getProperty("mqtt.broker");
        String clientId = ConfigLoader.getProperty("mqtt.clientId");
        String topic = ConfigLoader.getProperty("mqtt.topic");
        mqttClient = new MQTTClient(broker, clientId, topic);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Select the language:");
        System.out.println("1 - Italian");
        System.out.println("2 - English");
        System.out.println("Choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();
        scanner.close();

        String language = (choice == 1) ? "Italian" : "English";
        String MODEL_PATH = language.equals("Italian") ? modelPathIT : modelPathEN;

        System.out.println("Running instance with " + language + " model.");

        AudioCapture audio = new AudioCapture();
        SpeechToText speech = new SpeechToText(MODEL_PATH);

        boolean useAPI = language.equals("Italian");
        SentimentAnalysis sentiment = new SentimentAnalysis(useAPI);

        // Part for defining sentence length and pauses between sentences
        StringBuilder phrase = new StringBuilder();
        long lastSpeechTimestamp = System.currentTimeMillis();

        audio.start();

        LOGGER.info("Listening for audio...");

        byte[] buffer = new byte[4096];
        
        while(true) {
            int bytesRead = audio.read(buffer);
            
            if(bytesRead > 0) {
                String recognizedText = speech.processAudio(buffer);

                if (recognizedText != null && !recognizedText.isEmpty()) {
                    lastSpeechTimestamp = System.currentTimeMillis();

                    phrase.append(recognizedText).append(" ");

                    String[] words = phrase.toString().trim().split("\\s+");

                    if (words.length >= MIN_WORDS_THRESHOLD) {
                        String sentimentLabel = sentiment.analyzeSentiment(phrase.toString().trim());

                        sendToNodeRed(recognizedText, sentimentLabel, language);

                        LOGGER.info("Sent to Node-RED: " + phrase + " | Sentiment: " + sentimentLabel);

                        phrase.setLength(0);
                    }
                }
                // Check if there's an extended pause
                if (System.currentTimeMillis() - lastSpeechTimestamp > PAUSE_THRESHOLD && !phrase.isEmpty()) {
                    String sentimentLabel = sentiment.analyzeSentiment(phrase.toString().trim());

                    sendToNodeRed(recognizedText, sentimentLabel, language);

                    LOGGER.info("Sent to Node-RED: " + phrase + " | Sentiment: " + sentimentLabel);
                    phrase.setLength(0);
                }
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Interrupted thread: " + e.getMessage());
                }
            }
        }
    }

    private static void sendToNodeRed(String text, String sentiment, String language) {
        try {
            String jsonPayload = String.format("{\"text\": \"%s\", \"sentiment\": \"%s\", \"language\": \"%s\"}",
                    text, sentiment, language);
            mqttClient.publish(jsonPayload);
            LOGGER.info("Sent to MQTT: " + jsonPayload);
        } catch ( MqttException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}