package OrionLuouo.Craft.logic.input.SDP;

public abstract class StreamParser {
    StringBuilder builder;
    WordParser wordParser;
    GrammarParser grammarParser;
    StructuredDocumentParser structuredDocumentParser;

    /**
     * For some determined not needing StructuredDocumentParser object inner StreamParser.
     */
    StreamParser() {
    }

    protected StreamParser(StructuredDocumentParser structuredDocumentParser) {
        builder = new StringBuilder();
        this.structuredDocumentParser = structuredDocumentParser;
        wordParser = structuredDocumentParser.wordParser;
        grammarParser = structuredDocumentParser.grammarParser;
    }

    /**
     * The input stream will first go through this method,
     * and the content be recorded automatically.
     */
    void input(char character) {
        if (parse(character)) {
            builder.append(character);
        }
    }

    protected final void setStreamParser(StreamParser streamParser) {
        structuredDocumentParser.streamParser = streamParser;
    }

    /**
     * @return If the character is not a complete word itself
     *         ,and needs other inputs
     *         ,then return true.
     */
    protected abstract boolean parse(char character);

    /**
     * To truncate the input stream,
     * and send the word now into the next WordParser.
     */
    protected final void truncate() {
        wordParser.input(builder.toString());
        builder.setLength(0);
    }

    /**
     * To truncate the input stream,
     * and assume that you already know which type the word is of,
     * then send it into the GrammarParser directly.
     */
    protected final void truncate(WordParser.WordType type) {
        grammarParser.input(builder.toString() , type);
        builder.setLength(0);
    }

    /**
     * To append a word to the WordParser.
     */
    protected final void append(String word) {
        wordParser.input(word);
    }

    /**
     * To append a detected word to the GrammarParser.
     */
    protected final void append(String word, WordParser.WordType type) {
        grammarParser.input(word, type);
    }

    /**
     * To truncate the input stream,
     * but you have an alternative of the word now.
     */
    protected final void truncate(String word) {
        wordParser.input(word);
        builder.setLength(0);
    }

    /**
     * To truncate the input stream,
     * but you have an alternative of the word now.
     */
    protected final void truncate(String word , WordParser.WordType type) {
        grammarParser.input(word, type);
        builder.setLength(0);
    }

    /**
     * Get the word recorded.
     */
    protected final String wordNow() {
        return builder.toString();
    }
}

class InnerStreamParser extends StreamParser {
    InnerStreamParser(StructuredDocumentParser structuredDocumentParser) {
        super(structuredDocumentParser);
    }

    @Override
    protected boolean parse(char character) {
        return false;
    }
}