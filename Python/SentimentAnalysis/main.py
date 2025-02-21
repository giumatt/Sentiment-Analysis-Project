from src.audio_capture import AudioCapture
from src.speech_to_text import SpeechToText
from src.sentiment_analysis import SentimentAnalysis

import json
import os
import paho.mqtt.client as mqtt
import shutil
from dotenv import load_dotenv

load_dotenv()

MQTT_BROKER = "localhost"
MQTT_PORT = 1883
MQTT_TOPIC = "sentiment_analysis"

client = mqtt.Client()
#client.connect(MQTT_BROKER, MQTT_PORT, 60)

def send_to_nodered(data):
    payload = json.dumps(data)
    client.publish(MQTT_TOPIC, payload)
    print(f"Sent message to Node-RED '{MQTT_TOPIC}': {payload}")

def select_model(language):
    models = {
        "it": "../SentimentAnalysis/models/vosk-model-small-it-0.22",
        "en": "../SentimentAnalysis/models/vosk-model-en-us-daanzu-20200905"
    }

    if language not in models:
        print(f"Error: '{language}' not supported.")
    
    model_path = models[language]

    if not os.path.exists(model_path):
        raise FileNotFoundError(f"Model '{model_path}' not found.")

    if os.path.exists("model"):
        shutil.rmtree("model")
    
    shutil.copytree(model_path, "model")

def main():
    language = input("Select language [it/en]: ").strip().lower()
    select_model(language)

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
            text_data = speech.audio_to_text(audio_data)
            text_dict = json.loads(text_data)
            text = text_dict["text"]

            if text:
                print(f"Captured text: {text}")

                if language == "it":
                    lang = "Italian"
                else:
                    lang = "English"

                # Sentiment Analysis
                sentiment_result = sentiment.analyze_sentiment(text)
                print(f"Sentiment: {sentiment_result}")

                data = {
                    "text": text,
                    "sentiment": sentiment_result,
                    "language": lang
                }

                #send_to_nodered(data)
            else:
                print("Retry!")
        except KeyboardInterrupt:
            print("Exiting program...")
            break
    
if __name__ == "__main__":
    main()