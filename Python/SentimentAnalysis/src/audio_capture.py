import speech_recognition as sr
import time

class AudioCapture:
    def __init__(self, silence_threshold=5):
        self.recognizer = sr.Recognizer()
        self.microphone = sr.Microphone()
        self.silence_threshold = silence_threshold
        
    def adjust_noise(self):
        with self.microphone as source:
            self.recognizer.adjust_for_ambient_noise(source)
    
    def capture_audio(self, silence_threshold=2):
        with self.microphone as source:
            audio_buffer = []
            last_speech_time = time.time()
            
            while True:
                try:
                    audio = self.recognizer.listen(source, phrase_time_limit=10)
                    audio_buffer.append(audio)
                    print("FIN QUI ARRIVO")
                    last_speech_time = time.time()
                except sr.WaitTimeoutError:
                    pass

                if time.time() - last_speech_time > silence_threshold:
                    break
        
        if audio_buffer:
            combined_audio = audio_buffer[0]
            for segment in audio_buffer[1]:
                combined_audio = sr.AudioData(
                    combined_audio.frame_data + segment.frame_data,
                    combined_audio.sample_rate,
                    combined_audio.sample_width
                )
            return combined_audio
        
        return None