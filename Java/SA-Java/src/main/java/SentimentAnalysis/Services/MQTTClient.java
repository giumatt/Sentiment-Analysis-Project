package SentimentAnalysis.Services;

import SentimentAnalysis.Interfaces.MQTTClientInterface;
import org.eclipse.paho.client.mqttv3.*;

public class MQTTClient implements MQTTClientInterface {
    private final MqttClient client;
    private final String topic;

    public MQTTClient(String broker, String clientID, String topic) throws MqttException {
        this.client = new MqttClient(broker, clientID);
        this.topic = topic;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
    }

    public void publish(String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(1);
        client.publish(topic, mqttMessage);
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }
}
