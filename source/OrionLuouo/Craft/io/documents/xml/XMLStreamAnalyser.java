package OrionLuouo.Craft.io.documents.xml;

import java.io.IOException;
import java.util.*;

public class XMLStreamAnalyser implements XMLAnalyser {
    static final Set<Character> BLANK_CHARS = new HashSet<>(Arrays.asList(' ', '\r', '\n', (char) 9, (char) 0)), END_CHARS = new HashSet<>(Arrays.asList('/', '>'));

    Map<String, String> properties, parsingProperties;
    XMLDocument.Label root, parsingLabel;
    StringBuilder builder;
    int index;
    char[] chars;
    Parser parser;
    LinkedList<XMLDocument.Label> queue;
    String name;

    public XMLStreamAnalyser() {
        parser = new HeadParser();
        queue = new LinkedList<>();
        root = new XMLDocument.Label();
        parsingLabel = root;
    }

    /**
     * Input the next char stream.
     * The StreamParser will record parsing states,
     * so the stream can end at any point.
     * Even if the char array is not full,
     * this method can also work.
     * Character 0xu0000 is parsed as blank,
     * so it will be traversed and ignored.
     * But you'd better not input an array with too many uninitialized content,
     * that may cost lots of CPU time.
     *
     * @param chars
     * @return
     */
    public XMLStreamAnalyser input(char[] chars) throws IOException {
        if (chars == null || chars.length == 0)
            return this;
        this.chars = chars;
        index = 0;
        while (parser.parse()) ;
        chars = null;
        return this;
    }

    /**
     * If you summarize a document without invoking input() even once,
     * it will return a wrong document with null properties and root label.
     * Documents returned by multiple invoking will share the same properties.
     * e.g. Parsed a label "<a/>",
     * and summarized document A.
     * Then parsed label "<b/>",
     * and summarized document B.
     * That means,
     * A will have label "<a/>",
     * B will have labels "<a/>" and "<b/>".
     * If you change the properties of label "<a/>" by invoking methods of document A,
     * the label "<a/>" of document B will change too.
     *
     * @return
     */
    @Override
    public XMLDocument summarize() {
        return new Document(properties, root.getLabels()[0]);
    }

    private interface Parser {
        boolean parse() throws IOException;
    }

    private class StringMatcher {
        int i;
        char[] c;

        StringMatcher(String str) {
            c = str.toCharArray();
            i = 0;
        }

        byte compare() {
            while (i < c.length && index < chars.length)
                if (c[i++] != chars[index++])
                    return -1;
            if (i == c.length)
                return 1;
            return 0;
        }
    }

    private class HeadParser implements Parser {
        StringMatcher matcher;

        HeadParser() {
            matcher = new StringMatcher("<?xml");
        }

        @Override
        public boolean parse() throws IOException {
            if (matcher != null) {
                byte s;
                if ((s = matcher.compare()) == -1)
                    throw new IOException("Error : Not an XML document -- wrong document label.");
                if (s == 0)
                    return false;
                matcher = null;
                if (index >= chars.length)
                    return false;
            }
            while (BLANK_CHARS.contains(chars[index]))
                if (++index == chars.length)
                    return false;
            if (index == chars.length)
                return false;
            properties = new LinkedHashMap<>();
            parser = new HeadPropertyParser();
            return index != chars.length;
        }
    }

    private class PropertyParser implements Parser {
        String property;
        boolean stringBuilt;

        PropertyParser() {
            builder = new StringBuilder();
        }

        @Override
        public boolean parse() throws IOException {
            while (BLANK_CHARS.contains(chars[index]))
                if (++index == chars.length)
                    return false;
            while (true) {
                if (!stringBuilt)
                    while (!BLANK_CHARS.contains(chars[index]) && !END_CHARS.contains(chars[index]) && chars[index] != '=') {
                        builder.append(chars[index]);
                        if (++index == chars.length)
                            return false;
                    }
                stringBuilt = true;
                while (BLANK_CHARS.contains(chars[index]))
                    if (++index == chars.length)
                        return false;
                if (property != null) {
                    parsingProperties.put(property, builder.substring(1, builder.length() - 1));
                    property = null;
                    stringBuilt = false;
                    if (END_CHARS.contains(chars[index])) {
                        parser = new LabelBodyParser();
                        return index != chars.length;
                    }
                } else {
                    if (chars[index] == '=')
                        if (++index == chars.length)
                            return false;
                    while (BLANK_CHARS.contains(chars[index]))
                        if (++index == chars.length) {
                            parser = new BlankClearer(this);
                            return false;
                        }
                    property = builder.toString();
                    stringBuilt = false;
                }
                builder = new StringBuilder();
            }
        }
    }

    private class HeadPropertyParser extends PropertyParser {
        byte s = 0;
        StringMatcher matcher;
        boolean stringBuilt;

        HeadPropertyParser() {
            super();
        }

        @Override
        public boolean parse() throws IOException {
            lp:
            while (matcher == null) {
                if (!stringBuilt)
                    while (!BLANK_CHARS.contains(chars[index]) && chars[index] != '=') {
                        if (chars[index] == '?') {
                            matcher = new StringMatcher("?>");
                            break lp;
                        }
                        builder.append(chars[index]);
                        if (++index == chars.length)
                            return false;
                    }
                stringBuilt = true;
                while (BLANK_CHARS.contains(chars[index]))
                    if (++index == chars.length)
                        return false;
                if (property != null) {
                    properties.put(property, builder.substring(1, builder.length() - 1));
                    property = null;
                } else {
                    if (chars[index] == '=')
                        index++;
                    if (index >= chars.length)
                        return false;
                    while (BLANK_CHARS.contains(chars[index]))
                        if (++index == chars.length) {
                            parser = new BlankClearer(this);
                            return false;
                        }
                    property = builder.toString();
                }
                stringBuilt = false;
                builder = new StringBuilder();
            }
            if (s == 0) {
                if ((s = matcher.compare()) == -1)
                    throw new IOException("Error : Not an XML document -- wrong head ending.");
                if (s == 0)
                    return false;
            }
            if (index >= chars.length)
                return false;
            while (BLANK_CHARS.contains(chars[index]))
                if (++index == chars.length)
                    return false;
            parser = new BeginSignalParser();
            return index != chars.length;
        }
    }

    private class BlankClearer implements Parser {
        Parser p;

        BlankClearer(Parser parser) {
            p = parser;
        }

        @Override
        public boolean parse() {
            while (BLANK_CHARS.contains(chars[index]))
                if (++index == chars.length)
                    return false;
            parser = p;
            return index != chars.length;
        }
    }

    private class BeginSignalParser implements Parser {
        StringMatcher matcher;

        @Override
        public boolean parse() throws IOException {
            if (matcher == null) {
                if (chars[index] == '<' && ++index == chars.length)
                    return false;
                if (chars[index] != '!' && chars[index] != '/') {
                    parser = new LabelParser();
                    return index != chars.length;
                } else if (chars[index] == '!')
                    matcher = new StringMatcher("!--");
                else {
                    parser = new LabelEndParser();
                    return index != chars.length;
                }
            }
            byte s = matcher.compare();
            if (s == -1)
                throw new IOException("Error : Wrong format of notes.");
            if (s == 0)
                return false;
            parser = new NoteParser();
            return index != chars.length;
        }
    }

    private class LabelParser implements Parser {
        LabelParser() {
            builder = new StringBuilder();
            parsingProperties = new LinkedHashMap<>();
        }

        @Override
        public boolean parse() throws IOException {
            if (chars[index] == '<')
                index++;
            if (index >= chars.length)
                return false;
            while (!BLANK_CHARS.contains(chars[index])) {
                if (chars[index] == '>') {
                    parser = new LabelBodyParser();
                    name = builder.toString();
                    return index != chars.length;
                }
                builder.append(chars[index]);
                if (++index == chars.length)
                    return false;
            }
            name = builder.toString();
            parser = new PropertyParser();
            return index != chars.length;
        }
    }

    private class LabelBodyParser implements Parser {
        boolean alone;

        @Override
        public boolean parse() throws IOException {
            if (!alone) {
                if (chars[index] == '/') {
                    parsingLabel.addLabel(new XMLDocument.Label(name, parsingProperties));
                    alone = true;
                    index++;
                } else if (chars[index] == '>') {
                    queue.push(parsingLabel);
                    XMLDocument.Label label = parsingLabel;
                    parsingLabel = new XMLDocument.Label(name, parsingProperties);
                    label.addLabel(parsingLabel);
                    parser = new BlankParser();
                    index++;
                    return index != chars.length;
                }
            }
            if (index == chars.length)
                return false;
            if (chars[index] != '>')
                throw new IOException("Error : Wrong end of a label.");
            index++;
            parser = new BlankParser();
            return index != chars.length;
        }
    }

    private class BlankParser implements Parser {
        BlankParser() {
            builder = new StringBuilder();
        }

        @Override
        public boolean parse() throws IOException {
            while (BLANK_CHARS.contains(chars[index])) {
                builder.append(chars[index]);
                if (++index == chars.length)
                    return false;
            }
            parser = chars[index] == '<' ? new BeginSignalParser() : new TextParser();
            return index != chars.length;
        }
    }

    private class LabelEndParser implements Parser {
        StringMatcher matcher = new StringMatcher(parsingLabel.getName() + '>');

        LabelEndParser() {
            index++;
        }

        @Override
        public boolean parse() throws IOException {
            byte s = matcher.compare();
            if (s == -1)
                throw new IOException("Error : Wrong end of label.");
            if (s == 0)
                return false;
            parsingLabel = queue.pop();
            parser = new BlankParser();
            return index != chars.length;
        }
    }

    private class NoteParser implements Parser {
        StringMatcher matcher;
        byte s;

        @Override
        public boolean parse() throws IOException {
            if (matcher == null) {
                while (chars[index] != '-')
                    if (++index == chars.length)
                        return false;
                matcher = new StringMatcher("-->");
            }
            s = matcher.compare();
            if (s == 0)
                return false;
            if (s == -1) {
                matcher = null;
                return index != chars.length;
            }
            parser = new BlankParser();
            return index != chars.length;
        }
    }

    private class TextParser implements Parser {

        @Override
        public boolean parse() throws IOException {
            while (chars[index] != '<') {
                builder.append(chars[index]);
                if (++index == chars.length)
                    return false;
            }
            parsingLabel.setText(parsingLabel.getText() + builder.toString().trim());
            parser = new BeginSignalParser();
            return index != chars.length;
        }
    }
}