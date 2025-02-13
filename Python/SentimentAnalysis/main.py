from src.audio_capture import SentimentAnalysis.Services.SentimentAnalysis.Services.AudioCapture
from src.speech_to_text import SentimentAnalysis.Services.SpeechToText
from src.sentiment_analysis import SentimentAnalysis.Services.SentimentAnalysis

def main():
    audio = SentimentAnalysis.Services.SentimentAnalysis.Services.AudioCapture()
    speech = SentimentAnalysis.Services.SpeechToText()
    sentiment = SentimentAnalysis.Services.SentimentAnalysis()

    print("Adapting to environmental noises, wait...")
    audio.adjust_noise()
    print("System ready. Listening...")

    while True:
        try:
            audio_data = audio.capture_audio()
            text = speech.audio_to_text(audio_data)

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