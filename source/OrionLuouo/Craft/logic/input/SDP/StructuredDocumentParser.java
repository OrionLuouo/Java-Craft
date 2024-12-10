package OrionLuouo.Craft.logic.input.SDP;


import java.io.IOException;
import java.io.Reader;
import java.util.stream.Stream;

public class StructuredDocumentParser {
    StreamParser streamParser;
    WordParser wordParser;
    GrammarParser grammarParser;

    public StructuredDocumentParser() {
        wordParser = new WordParser();
        wordParser.grammarParser = grammarParser = new GrammarParser();
    }

    /**
     * To initialize the StreamParser.<p>
     * It should only be invoked before functioning.
     *
     * @throws SDPException If this method is invoked twice or more,<p>
     *                      it will throw a SDPException.
     */
    public void initializeStreamParser(StreamParser streamParser) throws SDPException {
        if (this.streamParser != null) {
            throw new SDPException("SDPException-Initialization: The StreamParser is already set.");
        }
        this.streamParser = streamParser;
    }

    public WordParser getWordParser() {
        return wordParser;
    }

    public void input(CharSequence text) {
        text.chars().forEach(character -> {
            streamParser.input((char) character);
        });
    }

    public void input(String text) {
        char[] characters = text.toCharArray();
        for (char character : characters) {
            streamParser.input(character);
        }
    }

    public void input(char... text) {
        for (char character : text) {
            streamParser.input(character);
        }
    }

    public void input(Stream<Character> stream) {
        stream.forEach(streamParser::input);
    }

    public void input(Reader reader) throws IOException {
        char[] buffer = new char[1024];
        int length;
        while((length = reader.read(buffer)) > 0) {
            for (int i = 0 ; i < length ; ) {
                streamParser.input(buffer[i++]);
            }
        }
    }

    /**
     * To signal the end of input.<p>
     * But this method is not that reliable,<p>
     * especially when the StreamParser system co-work poorly.<p>
     * So you can input some nonsense characters that act as the delimiter of elements,<p>
     * like blanks,<p>
     * before invoke this method.
     */
    public void endInput() {
        streamParser.endInput();
    }

    public static StructuredDocumentParser constructStandardParser() {
        StructuredDocumentParser structuredDocumentParser = new StructuredDocumentParser();
        StreamParserStandardSystem streamParserStandardSystem = new StreamParserStandardSystem(structuredDocumentParser);
        structuredDocumentParser.initializeStreamParser(streamParserStandardSystem.getStreamParser());
        structuredDocumentParser.getWordParser().addBasicMarkTypes();
        streamParserStandardSystem.addBasicEscapeFormat();
        return structuredDocumentParser;
    }
}
