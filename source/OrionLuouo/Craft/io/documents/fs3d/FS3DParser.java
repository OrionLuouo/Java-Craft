package OrionLuouo.Craft.io.documents.fs3d;

import OrionLuouo.Craft.io.documents.fs3d.source.DocumentStatement;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.stream.Stream;

public class FS3DParser {
    DocumentStatement statement;

    public FS3DParser(File file) throws IOException {
        this();
        parse(new FileReader(file));
    }

    public FS3DParser() {
        statement = new DocumentStatement();
    }

    public void parse(CharSequence sequence) {
        sequence.chars().forEach(c -> {
            statement.input((char) c);
        });
        }

    public void parse(Stream<Character> stream) {
        stream.forEach(statement::input);
    }

    public void parse(Reader reader) throws IOException {
        int value;
        while ((value = reader.read()) != -1) {
            statement.input(((char) value));
        }
    }

    public void parse(Iterator<Character> iterator) {
        iterator.forEachRemaining(statement::input);
    }

    public void parse(char c) {
        statement.input(c);
    }

    public boolean canBeSummarized() {
        return statement.canBeSummarized();
    }

    @Unfinished
    public FS3Document summarize() throws FS3DException{
        return null;
    }
}
