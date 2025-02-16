import serial
import base64
import numpy as np
import io
import wave

class AudioCapture:
    def __init__(self, port="/dev/ttyACM0", baudrate=115200):
        self.serial_port = serial.Serial(port, baudrate, timeout=1)
    
    def capture_audio(self, silence_threshold=2):
        try:
            line = self.serial_port.readline().strip()
            if not line:
                return None
            
            audio_bytes = base64.b64decode(line)
            return np.frombuffer(audio_bytes, dtype=np.uint16)
        
        except Exception as e:
            print(f"Error while audio capturing: {e}")
            return None