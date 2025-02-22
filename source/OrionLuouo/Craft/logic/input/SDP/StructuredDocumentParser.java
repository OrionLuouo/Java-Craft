package OrionLuouo.Craft.logic.input.SDP;


import OrionLuouo.Craft.system.annotation.Unfinished;

import java.io.IOException;
import java.io.Reader;
import java.util.stream.Stream;

/**
 * A parser of structured document with rules customized.<p>
 * It contains a chain of parsers,
 * which includes StreamParser, WordParser and SemanticRegex in order.<p>
 * The input stream will be parsed level by level,
 * and finally converted into execution of invoking the codes of your StateLayer.<p>
 * In the process,<p>
 * StreamParser can be customized,
 * yet only for efficiently processing some simple grammars.<p>
 * WordParser is unchangeable,
 * and stores and manage the elements defined in the documents.<p>
 * SemanticRegex is used to define the potential structure of any sentence in the document.<p>
 * They're fully customed and dynamic.<p>
 * Different regexes are needed in different situations.
 */
public class StructuredDocumentParser {
    StreamParser streamParser;
    WordParser wordParser;
    RegexLayer regexLayer;

    public StructuredDocumentParser() {
        wordParser = new WordParser();
    }

    public void setRegexLayer(RegexLayer regexLayer) {
        this.regexLayer = regexLayer;
        streamParser.setRegexLayer(regexLayer);
        wordParser.setRegexLayer(regexLayer);
    }

    /**
     * To initialize the StreamParser.<p>
     * It should only be invoked before the document parser's functioning.
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
