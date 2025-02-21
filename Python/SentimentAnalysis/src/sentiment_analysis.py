from transformers import pipeline

class SentimentAnalysis:
    def __init__(self):
        self.sentiment_pipeline = pipeline("text-classification", model="tabularisai/multilingual-sentiment-analysis")
    
    def analyze_sentiment(self, text):
        results = self.sentiment_pipeline(text)

        return results[0]['label']