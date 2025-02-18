from src.audio_capture import AudioCapture
from src.speech_to_text import SpeechToText
from src.sentiment_analysis import SentimentAnalysis
import langid
import json
import requests
import paho.mqtt.client as mqtt

MQTT_BROKER = "localhost"
MQTT_PORT = 1883
MQTT_TOPIC = "sentiment_analysis"

client = mqtt.Client()
client.connect(MQTT_BROKER, MQTT_PORT, 60)

def main():
    audio = AudioCapture()
    speech = SpeechToText()
    sentiment = SentimentAnalysis()

    print("Adapting to environmental noises, wait...")
    audio.adjust_noise()
    print("System ready. Listening...")

    while True:
        try:
            # Audio capture
            audio_data = audio.capture_audio()

            # Speech-to-Text
            text = speech.audio_to_text(audio_data)

            if text:
                print(f"Captured text: {text}")

                # Language identification
                language, _ = langid.classify(text)

                if language == "it":
                    lang = "Italian"
                else:
                    lang = "English"

                print(f"Detected language: {lang}")

                # Sentiment Analysis
                sentiment_result = sentiment.analyze_sentiment(text)
                print(f"Sentiment: {sentiment_result}")

                data = {
                    "text": text,
                    "sentiment": sentiment_result,
                    "language": lang
                }

                send_to_nodered(data)
            else:
                print("Retry!")
        except KeyboardInterrupt:
            print("Exiting program...")
            break

def send_to_nodered(data):
    payload = json.dumps(data)
    client.publish(MQTT_TOPIC, payload)
    print(f"Sent message to Node-RED '{MQTT_TOPIC}': {payload}")
    
if __name__ == "__main__":
    main()