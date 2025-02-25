from src.audio_capture import AudioCapture
from src.speech_to_text import SpeechToText
from src.sentiment_analysis import SentimentAnalysis

import json
import os
import paho.mqtt.client as mqtt
import shutil
from dotenv import load_dotenv

load_dotenv()

MQTT_BROKER = os.getenv("MQTT_BROKER")
MQTT_PORT = 1883
MQTT_TOPIC = "sentiment_analysis"

client = mqtt.Client()
client.connect(MQTT_BROKER, MQTT_PORT, 60)

def send_to_nodered(data):
    payload = json.dumps(data)
    client.publish(MQTT_TOPIC, payload)
    print(f"Sent message to Node-RED '{MQTT_TOPIC}': {payload}")

def select_model(language):
    models = {
        "it": os.getenv("VOSK_MODEL_IT"),
        "en": os.getenv("VOSK_MODEL_EN")
    }

    if language not in models:
        print(f"Error: '{language}' not supported.")
    
    model_path = models[language]

    if not os.path.exists(model_path):
        raise FileNotFoundError(f"Model '{model_path}' not found.")

    if os.path.exists("model"):
        shutil.rmtree("model")
    
    shutil.copytree(model_path, "model")

def capture_audio_chunk(audio_capture, num_frames=32):
    chunk = bytearray()
    for _ in range(num_frames):
        frame = audio_capture.capture_audio()
        if frame:
            chunk.extend(frame)
    return bytes(chunk)

def main():
    language = input("Select language [it/en]: ").strip().lower()
    select_model(language)

    audio = AudioCapture()
    speech = SpeechToText()
    sentiment = SentimentAnalysis()

    while True:
        try:
            # Audio capture
            raw_audio_data = capture_audio_chunk(audio, num_frames=157)
            
            # Speech-to-Text
            result = speech.audio_to_text(raw_audio_data)
            if result is None:
                print("Voice recognition failed. Retry!")
                continue

            try:
                # Il risultato è una stringa JSON; lo converto in dizionario
                result_dict = json.loads(result)
                text = result_dict.get("text", "")
            except Exception as e:
                print(f"Error while decoding: {e}")
                continue

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

                send_to_nodered(data)
            else:
                print("Retry!")
        except KeyboardInterrupt:
            print("Exiting program...")
            break
    
if __name__ == "__main__":
    main()