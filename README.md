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

## 1️⃣ Clone this repository

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

---

# Sentment Analysis Project

Il progetto si divide in due demo: una versione Java e una versione Python. Ho creato la repo alla radice che contiene entrambe le versioni.

Per prima cosa c'è bisogno di clonare questa repository usando il comando: 
```
git clone https://github.com/giumatt/Sentiment-Analysis-Project
```
Oppure scaricando il file [zip](https://github.com/giumatt/Sentiment-Analysis-Project/archive/refs/heads/main.zip) che dovrà essere estratto successivamente.

## Versione Java
Si può usare un qualsiasi IDE (Visual Studio Code, Eclipse, JetBrains IDEA,...).

Per installare le dovute librerie e poter eseguire il file c'è bisogno di una versione di [Java](https://www.oracle.com/java/technologies/downloads/#java21) installata
sul sistema e creare una variabile d'ambiente affinché l'IDE possa eseguirlo. [Guida](https://www.ibm.com/docs/it/was-liberty/core?topic=susmelbuc-setting-java-home-variable-liberty-collective-members-controllers#tasktwlp_java_reqs__steps-unordered__1).

Dopodiché va installato [Maven](https://maven.apache.org/download.cgi): basta estrarlo in qualche cartella (fuori dal progetto) e, come fatto per Java, va creata la variabile d'ambiente anche per Maven che punta al contenuto della cartella `apache-maven-3.9.9`.

Per testare se il tutto funziona basta aprire un terminale qualsiasi e digitare:
- `java --version`: dovrebbe venire stampata a schermo la versione di Java correntemente installata;
- `mvn -v`: dovrebbe venire stampata a schermo la versione di Maven correntemente installata.

In seguito bisogna aprire un terminale dentro la cartella: `<percorso-precedente>\Sentiment Analysis Project\Java\SA-Java` e digitare:
```
mvn install
```
Come ultima cosa va scaricato il modello di [Vosk](https://alphacephei.com/vosk/), da [qui](https://alphacephei.com/vosk/models/vosk-model-it-0.22.zip) (modello Italiano)
e, nel file `SentimentAnalysis.Services.SentimentAnalysis.Main.Main.java` va cambiato il percorso in base a dove l'avete scaricato ed estratto.

Infine sarà possibile importare le dovute librerie ed eseguire il progetto.

Per qualsiasi problema non esitate a contattarmi! 😁

## Versione Python

Questa versione è molto più semplice da utilizzare.

Dopo aver clonato il repository, usando un terminale bisogna andare nella cartella `Python` e digitare il comando:

```
python3 -m venv .
```

Terminata la creazione del virtual environment bisogna eseguire il comando per attivarlo (va fatto ogni volta, la prima volta che si esegue il programma):

Su Windows:
```
C:\> <venv>\Scripts\activate.bat
```

Su Linux/MacOS:
```
source bin/activate
```

Una volta attivato il virtual environment va eseguito il comando:
```
pip install -r requirements.txt
```

Dopodiché sarà possibile eseguire il programma.

---

#### N. B. Al momento (17/01/2025) entrambe le versioni non sono testate

### Da fare:
- Sistemare il sentiment di Java e Python. Per Python [link](https://huggingface.co/tabularisai/multilingual-sentiment-analysis) e per Java [link](https://stanfordnlp.github.io/CoreNLP/simple.html).
- Vedere per la dashboard (Node-RED(?)).
