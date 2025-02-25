import serial
import struct

class AudioCapture:
    def __init__(self, port="/dev/cu.usbmodem1201", baudrate=115200, frame_size=512):
        self.ser = serial.Serial(port, baudrate, timeout=1)
        self.frame_size = frame_size

    def capture_audio(self):
        # Attende il marker di inizio frame "STRT"
        while True:
            header = self.ser.read(4)
            if header == b'STRT':
                break
        # Legge la lunghezza dei campioni (2 byte, little-endian)
        len_bytes = self.ser.read(2)
        num_samples = struct.unpack('<H', len_bytes)[0]
        # Legge i dati audio: ogni campione occupa 2 byte (int16)
        data = self.ser.read(num_samples * 2)
        return data
