# 🏛️ Smart International Museum - Speech-to-Text & Sentiment Analysis

## 📌 Introduction

This project consists of two **independent implementations** in **Java** and **Python**, performing **audio capture**, **speech-to-text (STT)**, and **sentiment analysis**. The system is designed for **Smart International Museums**, where visitors can express opinions and feedback vocally in multiple languages. **Sentiment analysis** allows museum curators to understand visitor engagement and improve the experience. The use of **edge AI technologies** ensures **efficient local processing** without relying on cloud services.

The system records and analyzes **real-time feedback**, leveraging **AI models** for transcription and sentiment analysis.

## ⚙️ How It Works

Both Java and Python implementations perform the same tasks, with slight differences:

- **Java** captures audio from the **PC microphone**.
- **Python** captures audio from **Arduino Portenta H7**.

The applications:

1. **Record audio**
2. **Transcribe speech** (STT in Italian and English) using **Vosk**
3. **Analyze sentiment** with a **Hugging Face model**:
   - Java uses an **API** for sentiment analysis.
   - Python runs the model **locally**.
4. **Send data** (text, language, sentiment, timestamp) via **MQTT** to **Node-RED**.
5. **Store data in InfluxDB** and visualize it in a **Grafana** dashboard.

## 🏗️ Technologies Used

### Backend
- **Java** (Maven)
- **Python**
- **Vosk** (local STT)
- **Hugging Face Model** ([tabularisai/multilingual-sentiment-analysis](https://huggingface.co/tabularisai/multilingual-sentiment-analysis))

### Communication & Database
- **MQTT (Mosquitto)**
- **Node-RED**
- **InfluxDB**
- **Grafana**

### Containerization
- **Docker** with **docker-compose.yml** (`Docker` folder)

## 🚀 Installation

### 1️⃣ Clone this repository

```sh
git clone https://github.com/giumatt/Sentiment-Analysis-Project
```

### 2️⃣ Setup Docker Environment

Ensure **Docker** and **Docker Compose** are installed.

```sh
cd Docker
docker-compose up -d
```

### 3️⃣ Run Java Service

```sh
cd Java
mvn clean install
```

### 4️⃣ Run Python Service

```sh
cd Python
pip install -r requirements.txt
python main.py
```

## 📊 Data Visualization

1. Open **Node-RED** at `http://localhost:1880/`
2. Access **Grafana** at `http://localhost:3000/`
3. Analyze recorded data in **InfluxDB**

---

## 📚 References

- [Vosk Speech Recognition](https://alphacephei.com/vosk/)
- [Hugging Face - Sentiment Analysis Model](https://huggingface.co/tabularisai/multilingual-sentiment-analysis)
- [MQTT - Mosquitto](https://mosquitto.org/)
- [Node-RED](https://nodered.org/)
- [InfluxDB](https://www.influxdata.com/)
- [Grafana](https://grafana.com/)

---

💡 **Feel free to contribute or suggest improvements!** 🚀