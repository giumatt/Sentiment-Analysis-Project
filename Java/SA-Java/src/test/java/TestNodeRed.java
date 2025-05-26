import SentimentAnalysis.Services.MQTTClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class TestNodeRed {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883"; // assicurati che Node-RED ascolti qui
        String clientId = "javaClient";
        String topic = "sentiment_analysis";
        String payload = "{\"text\":\"Test messaggio\",\"sentiment\":\"neutral\",\"language\":\"Italian\"}";

        try {
            MQTTClient mqttClient = new MQTTClient(broker, clientId, topic);
            mqttClient.publish(payload);
            System.out.println("Messaggio pubblicato con successo su MQTT.");
            mqttClient.disconnect();
        } catch (MqttException e) {
            System.err.println("Errore durante la pubblicazione MQTT: " + e.getMessage());
            e.printStackTrace();
        }
    }
}