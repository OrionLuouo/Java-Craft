package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;

import java.util.*;

/**
 * A standard StreamParser system,
 * which is optimized from the inner package.
 * It includes parsing delimiters in the entrance method:input(char),
 * transmitting words directly to the WordParser,
 * and also annotation ignorance.
 * For security reasons these optimization points can't be publicized,
 * and it is not proper to encourage users override the StreamParser too much,
 * so this standard system is normally the best option.
 * But if you want a more customized StreamParser system,
 * and have determined that you exactly need to do it from StreamParser level but not GrammarParser level,
 * you can extend out a full system from StreamParser.
 */
public class StreamParserStandardSystem {
    public interface EscapeParser {
        void reset();

        /**
         * To decide whether the character corresponds to the escape string.
         *
         * @param character The content after the escape.
         *
         * @return If it corresponds,
         *         return 1,
         *         or return -1.
         *         When the escape string ends,
         *         return 0 to signal.
         */
        byte parse(char character);

        /**
         * @return The string after escaping.
         */
        String getText();
    }

    public static class FixedEscapeParser implements EscapeParser {
        final String format , meaning;
        int index;

        public FixedEscapeParser(String format, String meaning) {
            this.format = format;
            this.meaning = meaning;
        }

        @Override
        public void reset() {
            index = 0;
        }

        @Override
        public byte parse(char character) {
            if (character == format.charAt(index++)) {
                if (index == format.length()) {
                    return 0;
                }
                return 1;
            }
            return -1;
        }

        @Override
        public String getText() {
            return meaning;
        }
    }

    BlankStreamParser blankStreamParser;
    PreLineStructureStreamParser preLineAnnotationStreamParser;
    LineStructureStreamParser lineAnnotationStreamParser;
    PreAreaStreamParser preAreaAnnotationStreamParser;
    AreaAnnotationStreamParser areaAnnotationStreamParser;
    AreaEndStreamParser areaAnnotationEndStreamParser;
    PreAreaStreamParser preEscapedStringStreamParser;
    EscapedStringEndStreamParser escapedStringEndStreamParser;
    EscapedStringStreamParser escapedStringStreamParser;
    EscapeStreamParser escapeStreamParser;

    public StreamParserStandardSystem(StructuredDocumentParser structuredDocumentParser) {
        blankStreamParser = new BlankStreamParser(structuredDocumentParser , this);
        blankStreamParser.preLineAnnotationStreamParser = preLineAnnotationStreamParser = new PreLineStructureStreamParser(structuredDocumentParser , blankStreamParser);
        blankStreamParser.preAreaAnnotationStreamParser = preAreaAnnotationStreamParser = new PreAreaStreamParser(structuredDocumentParser , blankStreamParser);
        preLineAnnotationStreamParser.lineAnnotationStreamParser = lineAnnotationStreamParser = new LineStructureStreamParser(structuredDocumentParser , blankStreamParser);
        preAreaAnnotationStreamParser.areaStreamParser = areaAnnotationStreamParser = new AreaAnnotationStreamParser(structuredDocumentParser);
        areaAnnotationStreamParser.areaEndStreamParser = areaAnnotationEndStreamParser = new AreaEndStreamParser(structuredDocumentParser , blankStreamParser , preAreaAnnotationStreamParser.areaStreamParser);
        blankStreamParser.preEscapedStringStreamParser = new PreAreaStreamParser(structuredDocumentParser , blankStreamParser);
        preEscapedStringStreamParser.areaStreamParser = escapedStringStreamParser = new EscapedStringStreamParser(structuredDocumentParser);
        escapedStringStreamParser.areaEndStreamParser = escapedStringEndStreamParser = new EscapedStringEndStreamParser(structuredDocumentParser , blankStreamParser , escapedStringStreamParser);
        escapedStringEndStreamParser.escapedStringStreamParser = escapedStringStreamParser;
        escapeStreamParser = escapedStringStreamParser.escapeStreamParser;
        setLineAnnotationFormat("//");
        setAreaAnnotationFormat("/*" , "*/");
        setStringFormat("\"" , "\"");
        setEscape('\\');
    }

    public StreamParser getStreamParser() {
        return blankStreamParser;
    }

    public void setBlanks(Collection<Character> blanks) {
        blankStreamParser.blanks = BlankStreamParser.compileDelimiters(blanks);
    }

    public void setDelimiters(Collection<Character> delimiters) {
        blankStreamParser.delimiters = BlankStreamParser.compileDelimiters(delimiters);
    }

    public void setLineAnnotationFormat(String head) {
        blankStreamParser.lineAnnotationHead = head.charAt(0);
        blankStreamParser.preLineAnnotationStreamParser.head = head.toCharArray();
    }

    public void setAreaAnnotationFormat(String head , String end) {
        blankStreamParser.areaAnnotationEnd = head.charAt(0);
        preAreaAnnotationStreamParser.head = head.toCharArray();
        areaAnnotationStreamParser.end = end.charAt(0);
        areaAnnotationEndStreamParser.end = end.toCharArray();
    }

    public void setStringFormat(String head , String end) {
        blankStreamParser.stringBound = head.charAt(0);
        preEscapedStringStreamParser.head = head.toCharArray();
        escapedStringStreamParser.end = end.charAt(0);
        escapedStringEndStreamParser.end = end.toCharArray();
    }

    public void setEscape(char escape) {
        escapedStringStreamParser.escape = escape;
    }

    public void enableLineAnnotation(boolean enable) {
        blankStreamParser.preLineAnnotationStreamParser = enable ? preLineAnnotationStreamParser : blankStreamParser;
    }

    public void enableAreaAnnotation(boolean enable) {
        blankStreamParser.preAreaAnnotationStreamParser = enable ? preAreaAnnotationStreamParser : blankStreamParser;
    }

    public void enableEscapedStringStream(boolean enable) {
        blankStreamParser.preEscapedStringStreamParser = enable ? preEscapedStringStreamParser : blankStreamParser;
    }

    /**
     * The parsers will be stored in a List,
     * and tested serially at the beginning of an escape string.
     * One thing to be noticed is that as long as the format of the escape string is decided,
     * it is unchangeable,
     * which means once the parser fails,
     * the escaping fails.
     * So if two or more formats share the same head,
     * their parsing mechanisms should be built in a single complex parser.
     */
    public void addEscapeFormat(EscapeParser escapeParser) {
        escapeParser.reset();
        escapeStreamParser.escapeParsers.add(escapeParser);
    }
}

class BlankStreamParser extends PreAreaStreamParser {
    public static final boolean[] DEFAULT_BLANKS = compileDelimiters(Arrays.asList(' ' , '\r' , '\t' , '\n')) , DEFAULT_DELIMITERS = compileDelimiters(Arrays.asList(' ' , '\r' , '\t' , '\n' , ',' , '.' , '/' , '\\' , '|' , '<' , '>' , '?' , '!' , '"' , '$' , '%' , '^' , '&' , '*' , '(' , ')' , '-' , '=' , '+' , '[' , ']' , '{' , '}' , '#' , '~' , ';' , '\'' , ':' , '@'));

    static boolean[] compileDelimiters(Collection<Character> delimiters) {
        final char[] max = new char[1];
        delimiters.forEach(delimiter -> {
            max[0] = max[0] > delimiter ? max[0] : delimiter;
        });
        final boolean[] array = new boolean[max[0]];
        delimiters.forEach(delimiter -> {
            array[delimiter] = true;
        });
        return array;
    }

    StreamParserStandardSystem streamParserStandardSystem;
    boolean[] delimiters , blanks;
    char lineAnnotationHead , areaAnnotationEnd , stringBound;
    PreLineStructureStreamParser preLineAnnotationStreamParser;
    PreAreaStreamParser preAreaAnnotationStreamParser , preEscapedStringStreamParser;
    LineStructureStreamParser lineAnnotationStreamParser;
    AreaStreamParser areaAnnotationStreamParser , escapedStringStreamParser;


    static char exclude(char[] characters) {
        Set<Character> set = new HashSet<>();
        for (char character : characters) {
            set.add(character);
        }
        for (char character = 0 ; character < Character.MAX_VALUE ; ) {
            if (set.contains(character)) {
                return character;
            }
            character++;
        }
        throw new SDPException("SDPException-InnerError: The StreamParserStandardSystem tried to take a short-cut while processing annotation, but the preset annotation head occupies all characters available. Try to set a less complex annotation head.");
    }

    protected BlankStreamParser(StructuredDocumentParser structuredDocumentParser , StreamParserStandardSystem streamParserStandardSystem) {
        super(structuredDocumentParser , null);
        this.streamParserStandardSystem = streamParserStandardSystem;
        delimiters = DEFAULT_DELIMITERS;
        blanks = DEFAULT_BLANKS;
    }

    @Override
    void input(char character) {
        if (character == lineAnnotationHead) {
            preLineAnnotationStreamParser.index = 1;
            setStreamParser(preLineAnnotationStreamParser);
            return;
        }
        if (character == areaAnnotationEnd) {
            preAreaAnnotationStreamParser.index = 1;
            setStreamParser(preAreaAnnotationStreamParser);
            return;
        }
        if (character == stringBound) {
            preEscapedStringStreamParser.index = 1;
            setStreamParser(preEscapedStringStreamParser);
            return;
        }
        if (character > delimiters.length || !delimiters[character]) {
            builder.append(character);
        }
        else {
            if (!builder.isEmpty()) {
                wordParser.input(builder.toString());
                builder.setLength(0);
            }
            if (!blanks[character]) {
                wordParser.input(String.valueOf(character));
            }
        }
    }
}

class PreLineStructureStreamParser extends InnerStreamParser {
    BlankStreamParser blankStreamParser;
    LineStructureStreamParser lineAnnotationStreamParser;
    int index;
    char[] head;

    protected PreLineStructureStreamParser(StructuredDocumentParser structuredDocumentParser , BlankStreamParser blankStreamParser) {
        super(structuredDocumentParser);
        this.blankStreamParser = blankStreamParser;
    }

    @Override
    void input(char character) {
        if (character == head[index++]) {
            if (index == head.length) {
                setStreamParser(lineAnnotationStreamParser);
            }
        }
        else {
            setStreamParser(blankStreamParser);
            blankStreamParser.lineAnnotationHead = BlankStreamParser.exclude(head);
            for (int index = 0 ; index < this.index ; ) {
                blankStreamParser.input(head[index++]);
            }
            blankStreamParser.lineAnnotationHead = head[0];
        }
    }
}

class LineStructureStreamParser extends InnerStreamParser {
    BlankStreamParser blankStreamParser;

    protected LineStructureStreamParser(StructuredDocumentParser structuredDocumentParser , BlankStreamParser blankStreamParser) {
        super(structuredDocumentParser);
        this.blankStreamParser = blankStreamParser;
    }

    @Override
    void input(char character) {
        if (character == '\r' || character == '\n') {
            setStreamParser(blankStreamParser);
        }
    }
}

class PreAreaStreamParser extends PreLineStructureStreamParser {
    BlankStreamParser blankStreamParser;
    AreaStreamParser areaStreamParser;

    protected PreAreaStreamParser(StructuredDocumentParser structuredDocumentParser , BlankStreamParser blankStreamParser) {
        super(structuredDocumentParser , blankStreamParser);
        this.blankStreamParser = blankStreamParser;
    }

    @Override
    void input(char character) {
        if (index == head.length) {
            areaStreamParser.enter();
            setStreamParser(areaStreamParser);
            areaStreamParser.input(character);
            return;
        }
        if (character == head[index++]) {
            return;
        }
        setStreamParser(blankStreamParser);
        blankStreamParser.lineAnnotationHead = BlankStreamParser.exclude(blankStreamParser.preLineAnnotationStreamParser.head);
        blankStreamParser.areaAnnotationEnd = BlankStreamParser.exclude(head);
        for (int index = 0 ; index < this.index ; ) {
            blankStreamParser.input(head[index++]);
        }
        blankStreamParser.lineAnnotationHead = blankStreamParser.preLineAnnotationStreamParser.head[0];
        blankStreamParser.areaAnnotationEnd = head[0];
    }
}

abstract class AreaStreamParser extends LineStructureStreamParser {
    char end;
    AreaEndStreamParser areaEndStreamParser;

    AreaStreamParser(StructuredDocumentParser structuredDocumentParser) {
        super(structuredDocumentParser , null);
    }

    @Override
    abstract void input(char character);

    abstract void enter();
}

class AreaEndStreamParser extends InnerStreamParser {
    BlankStreamParser blankStreamParser;
    AreaStreamParser areaStreamParser;
    int index = 0;
    char[] end;

    protected AreaEndStreamParser(StructuredDocumentParser structuredDocumentParser , BlankStreamParser blankStreamParser , AreaStreamParser streamParser) {
        super(structuredDocumentParser);
        this.blankStreamParser = blankStreamParser;
        areaStreamParser = streamParser;
    }

    @Override
    void input(char character) {
        if (index == end.length) {
            setStreamParser(blankStreamParser);
            blankStreamParser.input(character);
            return;
        }
        if (character == end[index++]) {
        }
        else {
            areaStreamParser.end = BlankStreamParser.exclude(end);
            setStreamParser(areaStreamParser);
            for (int index = 0 ; index < this.index ; ) {
                blankStreamParser.input(end[index++]);
            }
            areaStreamParser.end = end[0];
        }
    }
}

class AreaAnnotationStreamParser extends AreaStreamParser {
    protected AreaAnnotationStreamParser(StructuredDocumentParser structuredDocumentParser) {
        super(structuredDocumentParser);
    }

    @Override
    void input(char character) {
        if (character == end) {
            areaEndStreamParser.index = 1;
            setStreamParser(areaEndStreamParser);
        }
    }

    @Override
    void enter() {
    }
}

class EscapedStringStreamParser extends AreaStreamParser {
    StringBuilder stringBuilder;
    EscapeStreamParser escapeStreamParser;
    char escape;

    EscapedStringStreamParser(StructuredDocumentParser structuredDocumentParser) {
        super(structuredDocumentParser);
        builder = new StringBuilder();
        escapeStreamParser = new EscapeStreamParser(structuredDocumentParser , stringBuilder , this);
    }

    @Override
    void input(char character) {
        if (character == escape) {
            setStreamParser(escapeStreamParser);
            return;
        }
        if (character == end) {
            areaEndStreamParser.index = 1;
            setStreamParser(areaEndStreamParser);
            return;
        }
        stringBuilder.append(character);
    }

    @Override
    void enter() {
        stringBuilder.setLength(0);
    }
}

class EscapedStringEndStreamParser extends AreaEndStreamParser {
    EscapedStringStreamParser escapedStringStreamParser;

    protected EscapedStringEndStreamParser(StructuredDocumentParser structuredDocumentParser, BlankStreamParser blankStreamParser, AreaStreamParser streamParser) {
        super(structuredDocumentParser, blankStreamParser, streamParser);
    }

    @Override
    void input(char character) {
        if (index == end.length) {
            structuredDocumentParser.grammarParser.input(escapedStringStreamParser.stringBuilder.toString() , structuredDocumentParser.wordParser.getType("String"));
            setStreamParser(blankStreamParser);
            blankStreamParser.input(character);
            return;
        }
        if (character == end[index++]) {
        }
        else {
            areaStreamParser.end = BlankStreamParser.exclude(end);
            setStreamParser(areaStreamParser);
            for (int index = 0 ; index < this.index ; ) {
                blankStreamParser.input(end[index++]);
            }
            areaStreamParser.end = end[0];
        }
    }
}

class EscapeStreamParser extends InnerStreamParser {
    EscapedStringStreamParser escapedStringStreamParser;
    List<StreamParserStandardSystem.EscapeParser> escapeParsers;
    StreamParserStandardSystem.EscapeParser parser;
    StringBuilder builder;

    EscapeStreamParser(StructuredDocumentParser structuredDocumentParser , StringBuilder stringBuilder , EscapedStringStreamParser escapeStreamParser) {
        super(structuredDocumentParser);
        this.escapedStringStreamParser = escapeStreamParser;
        escapeParsers = new ChunkChainList<>();
        builder = new StringBuilder();
    }

    @Override
    void input(char character) {
        if (parser == null) {
            builder.setLength(0);
            escapeParsers.iterateInterruptibly(escapeParser -> {
                int status;
                if ((status = escapeParser.parse(character)) > -1) {
                    if (status == 0) {
                        escapeParser.reset();
                        escapedStringStreamParser.stringBuilder.append(escapeParser.getText());
                        setStreamParser(escapedStringStreamParser);
                        return false;
                    }
                    parser = escapeParser;
                    return false;
                }
                escapeParser.reset();
                return true;
            });
            return;
        }
        builder.append(character);
        int status = parser.parse(character);
        if (status == 0) {
            escapedStringStreamParser.stringBuilder.append(parser.getText());
            parser.reset();
            parser = null;
            setStreamParser(escapedStringStreamParser);
        }
        else if (status == 1) {
        }
        else {
            parser.reset();
            parser = null;
            escapedStringStreamParser.stringBuilder.append(builder);
            setStreamParser(escapedStringStreamParser);
        }
    }
}
