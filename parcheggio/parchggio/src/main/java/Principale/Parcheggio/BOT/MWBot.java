package Principale.Parcheggio.BOT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

public class MWBot {

    @Value("${mqtt.broker.url}")
    private String mqttBrokerUrl;

    @Value("${mqtt.topic}")
    private String mqttTopic;

    private MqttClient mqttClient;

    public MWBot() {
        // Default constructor
    }

    // Initialize the MQTT client
    public void initialize() {
        try {
            mqttClient = new MqttClient(mqttBrokerUrl, MqttClient.generateClientId());
            mqttClient.connect();
        } catch (MqttException e) {
            System.err.println("Error initializing MQTT client: " + e.getMessage());
        }
    }

    @Async
    public void processRequest(long waitTimeMillis, String userId) {
        try {
            System.out.println("MWBot is waiting for " + waitTimeMillis + " milliseconds...");
            Thread.sleep(waitTimeMillis);

            String messageContent = "MWBot process completed for user: " + userId;
            sendMqttMessage(messageContent);

            System.out.println("MWBot finished processing and sent a message to the user.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("MWBot was interrupted: " + e.getMessage());
        } catch (MqttException e) {
            System.err.println("Error sending MQTT message: " + e.getMessage());
        }
    }

    private void sendMqttMessage(String messageContent) throws MqttException {
        if (mqttClient != null && mqttClient.isConnected()) {
            MqttMessage message = new MqttMessage(messageContent.getBytes());
            message.setQos(1);
            mqttClient.publish(mqttTopic, message);
        } else {
            System.err.println("MQTT client is not connected. Unable to send message.");
        }
    }

    public void close() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            System.err.println("Error disconnecting MQTT client: " + e.getMessage());
        }
    }
}

