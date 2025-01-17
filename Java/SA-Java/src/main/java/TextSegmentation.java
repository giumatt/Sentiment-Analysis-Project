public class TextSegmentation {
    public boolean isEndOfSentence(String text) {
        // We consider a dot or an exclamation mark as sentence endings
        return text.endsWith(".") || text.endsWith("!");
    }
}
