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
        grammarParser = new GrammarParser();
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
}
