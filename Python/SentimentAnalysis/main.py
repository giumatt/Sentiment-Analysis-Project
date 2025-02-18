from src.audio_capture import AudioCapture
from src.speech_to_text import SpeechToText
from src.sentiment_analysis import SentimentAnalysis

def main():
    audio = AudioCapture()
    speech = SpeechToText()
    sentiment = SentimentAnalysis()

    #print("Adapting to environmental noises, wait...")
    #audio.adjust_noise()
    #print("System ready. Listening...")

    while True:
        try:
            #audio_data = audio.capture_audio()
            #text = speech.audio_to_text(audio_data)
            text = "Questo dipinto non mi piace molto"
            if text:
                print(f"Captured text: {text}")
                sentiment_result = sentiment.analyze_sentiment(text)
                print(f"Sentiment: {sentiment_result}")
            else:
                print("Retry!")
        except KeyboardInterrupt:
            print("Exiting program...")
            break

if __name__ == "__main__":
    main()