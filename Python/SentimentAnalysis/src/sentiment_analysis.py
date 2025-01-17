from textblob import TextBlob

class SentimentAnalysis:
    @staticmethod
    def analyze_sentiment(text):
        blob = TextBlob(text)
        polarity = blob.sentiment.polarity

        if polarity > 0:
            return "Positive feedback"
        elif polarity < 0:
            return "Negative feedback"
        else:
            return "Neutral feedback"