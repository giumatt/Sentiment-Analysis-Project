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

In seguito bisogna aprire un terminale dentro la cartella: `<percorso-precedente>\Sentiment Analysis Project\Java\sa-java` e digitare:
```
mvn install
```
Infine sarà possibile importare le dovute librerie e soprattutto eseguire il progetto.

Per qualsiasi problema non esitate a contattarmi, sono sempre disponibile! 😁
