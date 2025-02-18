import speech_recognition as sr

class SpeechToText:
    def __init__(self):
        self.recognizer = sr.Recognizer()
    
    def audio_to_text(self, audio_data):
        try:
            return self.recognizer.recognize_vosk(audio_data, language="it-IT")
        except sr.UnknownValueError:
            return None
        except sr.RequestError as e:
            print(f"Error in the voice recognition service: {e}")
            return None