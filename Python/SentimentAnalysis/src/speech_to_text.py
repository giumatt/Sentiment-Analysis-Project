import os
import speech_recognition as sr
from vosk import Model, KaldiRecognizer

class SpeechToText:
    def __init__(self):
        model_path = "/home/mattia/Documents/Projects/Sentiment-Analysis-Project/Python/SentimentAnalysis/models/vosk-model-small-it-0.22"  # Assicurati che il percorso sia corretto
        
        if not os.path.exists(model_path):
            raise FileNotFoundError(f"Il modello Vosk non è stato trovato nel percorso: {model_path}")

        # Carica il modello Vosk
        self.model = Model(model_path)

    def audio_to_text(self, audio_data):
        try:
            # Crea il riconoscitore Vosk con il modello e il sample rate dell'audio
            recognizer = KaldiRecognizer(self.model, audio_data.sample_rate)

            # Passa i dati audio al riconoscitore
            if recognizer.AcceptWaveform(audio_data.get_raw_data()):
                result = recognizer.Result()
            else:
                result = recognizer.PartialResult()  # Usa anche i risultati parziali

            # Estrai solo il testo dalla stringa restituita
            text = result.split('"text":')[-1].replace('"', '').replace('}', '').strip()

            return text if text else None  # Se vuoto, restituisci None

        except Exception as e:
            print(f"Errore nel riconoscimento vocale: {e}")
            return None
