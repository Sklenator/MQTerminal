package com.rusnok4d.mqterminal;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;

public class MqttHandler {
    MqttClient client;

    String distance = "0.0";

    String temperature = "0.0";

    public void connect(String brokerUrl, String clientId) {
        try {
            // Set up the persistence layer
            MemoryPersistence persistence = new MemoryPersistence();

            // Initialize the MQTT client
            client = new MqttClient(brokerUrl, clientId, persistence);

            // Set up the connection options
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            // Connect to the broker
            client.connect(connectOptions);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic, 1);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.i("MQTTMessage", "connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    try {
                        System.out.println("MQTT:message:"+message.toString());
                        if(topic.equals("smarthome/basement/distance")){
                            distance = message.toString();
                        }
                        if(topic.equals("smarthome/temperature")){
                            temperature = message.toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.i("MQTT", "msg delivered");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
