#include <PDM.h>

#define FRAME_SIZE 512 // Buffer size for audio frames
int16_t audioBuffer[FRAME_SIZE]; // Buffer to store audio samples

void setup() {
  Serial.begin(115200); // Initialize serial communication
  while (!Serial);

  // Initialize PDM microphone with 1 channel and 16 kHz sampling rate
  if (!PDM.begin(1, 16000)) {
    Serial.println("Error while initializing PDM!");
    while (1);
  }

  // Set PDM buffer size based on the frame size
  PDM.setBufferSize(FRAME_SIZE * sizeof(int16_t));

  Serial.println("PDM correctly initialized.");
}

void loop() {
  // Check if there are enough bytes available in the PDM buffer
  int bytesAvailable = PDM.available();
  if (bytesAvailable >= FRAME_SIZE * sizeof(int16_t)) { 
    PDM.read(audioBuffer, FRAME_SIZE * sizeof(int16_t)); // Read audio data into the buffer
    
    Serial.write("STRT", 4); // Send a 4-byte header indicating the start of an audio frame
    
    uint16_t numSamples = FRAME_SIZE; // Number of samples in the frame
    Serial.write((uint8_t *)&numSamples, sizeof(numSamples)); // Send the sample count (2 bytes)
    
    Serial.write((uint8_t *)audioBuffer, FRAME_SIZE * sizeof(int16_t)); // Transmit the audio data
  }
}
