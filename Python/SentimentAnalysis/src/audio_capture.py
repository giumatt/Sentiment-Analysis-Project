import speech_recognition as sr

class AudioCapture:
    def __init__(self):
        self.recognizer = sr.Recognizer()
        self.microphone = sr.Microphone()

    def adjust_noise(self):
        with self.microphone as source:
            self.recognizer.adjust_for_ambient_noise(source)
    
    def capture_audio(self):                        # Find a way to extend the audio capture window
        with self.microphone as source:
            return self.recognizer.listen(source)