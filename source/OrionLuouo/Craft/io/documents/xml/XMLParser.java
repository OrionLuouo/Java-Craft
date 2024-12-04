package OrionLuouo.Craft.io.documents.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;

public interface XMLParser {
    default void input(char[] characters) {
        for (char character : characters) {
            input(character);
        }
    }

    default void input(String string) {
        for (char character : string.toCharArray()) {
            input(character);
        }
    }

    default void input(CharSequence sequence) {
        sequence.chars().forEach(character -> {
            input((char) character);
        });
    }

    default void input(InputStream inputStream) throws IOException {
        input(new InputStreamReader(inputStream));
    }

    default void input(Reader reader) throws IOException {
        int character;
        while ((character = reader.read()) != 0) {

        }
    }

    default void input(Stream<Character> stream) {

    }

    void input(char character);
}
