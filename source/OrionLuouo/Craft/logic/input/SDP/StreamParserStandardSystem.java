package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.system.annotation.Warning;

import java.util.*;

/**
 * A standard StreamParser system,<p>
 * which is optimized from the inner package.<p>
 * It includes parsing delimiters in the entrance method:input(char),<p>
 * transmitting words directly to the WordParser,<p>
 * and also annotation ignorance.<p>
 * For security reasons these optimization points can't be publicized,<p>
 * and it is not proper to encourage users override the StreamParser too much,<p>
 * so this standard system is normally the best option.<p>
 * But if you want a more customized StreamParser system,<p>
 * and have determined that you exactly need to do it from StreamParser level but not GrammarParser level,<p>
 * you can extend out a full system from StreamParser.
 */
@Warning(state = Warning.State.UNTESTED , information = "Not fully tested, yet seems to be fine.")
public class StreamParserStandardSystem {
    public enum EscapeParsingState {
        INCOMPLETE , NOT_CORRESPOND , COMPLETE , OVERFLOWN
    }

    public interface EscapeParser {
        void reset();

        /**
         * To decide whether the character corresponds to the escape string.
         *
         * @param character The content after the escape.
         *
         * @return If it corresponds,
         *         yet incomplete,
         *         return INCOMPLETE,
         *         or return NOT_CORRESPOND.
         *         When the escape string ends,
         *         return COMPLETE to signal.
         *         And there's a special situation can be OVERFLOWN:
         *         if the escape format is dynamic and unpredictable,
         *         and only when an unrelated character is inputted,
         *         can the parser determine the escape ends.
         *
         */
        EscapeParsingState parse(char character);

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
        public EscapeParsingState parse(char character) {
            if (character == format.charAt(index++)) {
                if (index == format.length()) {
                    return EscapeParsingState.COMPLETE;
                }
                return EscapeParsingState.INCOMPLETE;
            }
            return EscapeParsingState.NOT_CORRESPOND;
        }

        @Override
        public String getText() {
            return meaning;
        }
    }

    public static final EscapeParser ESCAPE_BACKSLASH = new FixedEscapeParser("\\" , "\\")
            , ESCAPE_ENTER = new FixedEscapeParser("r" , "\r")
            , ESCAPE_LINEFEED = new FixedEscapeParser("n" , "\n")
            , ESCAPE_TABLE = new FixedEscapeParser("t" , "\t")
            , ESCAPE_QUOTE = new FixedEscapeParser("\"" , "\"")
            , ESCAPE_UNICODE = new EscapeParser() {
        StringBuilder builder;
        boolean ued;
        char result;

        {
            builder = new StringBuilder();
        }

        @Override
        public void reset() {
            builder.setLength(0);
            ued = false;
        }

        @Override
        public EscapeParsingState parse(char character) {
            if (character <= '9' && character >= '0' && builder.length() < 6 && ued) {
                builder.append(character);
            }
            else if (builder.isEmpty() && character == 'u') {
                ued = true;
            }
            else {
                if (builder.isEmpty()) {
                    return EscapeParsingState.NOT_CORRESPOND;
                }
                result = (char) (Integer.parseInt(builder.toString()));
                return EscapeParsingState.OVERFLOWN;
            }
            return EscapeParsingState.INCOMPLETE;
        }

        @Override
        public String getText() {
            return String.valueOf(result);
        }
    };

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
        preEscapedStringStreamParser = blankStreamParser.preEscapedStringStreamParser = new PreEscapedStringStreamParser(structuredDocumentParser , blankStreamParser);
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

    /**
     * If normalize the content that looks like an escape,
     * but mismatched.
     * Once true and the situation occurs,
     * the content will be appended to the string.
     */
    public void setNormalizeUnknownEscape(boolean normalizeUnknownEscape) {
        escapeStreamParser.normalizeUnknownEscape = normalizeUnknownEscape;
    }

    public void addBasicEscapeFormat() {
        addEscapeFormat(ESCAPE_BACKSLASH);
        addEscapeFormat(ESCAPE_ENTER);
        addEscapeFormat(ESCAPE_LINEFEED);
        addEscapeFormat(ESCAPE_TABLE);
        addEscapeFormat(ESCAPE_QUOTE);
        addEscapeFormat(ESCAPE_UNICODE);
    }
}

class BlankStreamParser extends PreAreaStreamParser {
    public static final boolean[] DEFAULT_BLANKS = compileDelimiters(Arrays.asList(' ' , '\r' , '\t' , '\n')) , DEFAULT_DELIMITERS = compileDelimiters(Arrays.asList(' ' , '\r' , '\t' , '\n' , ',' , '.' , '/' , '\\' , '|' , '<' , '>' , '?' , '!' , '"' , '$' , '%' , '^' , '&' , '*' , '(' , ')' , '-' , '=' , '+' , '[' , ']' , '{' , '}' , '#' , '~' , ';' , '\'' , ':' , '@'));

    static boolean[] compileDelimiters(Collection<Character> delimiters) {
        final char[] max = new char[1];
        delimiters.forEach(delimiter -> {
            max[0] = max[0] > delimiter ? max[0] : delimiter;
        });
        final boolean[] array = new boolean[max[0] + 1];
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

    @Override
    void hidePreParsers() {
        lineAnnotationHead = BlankStreamParser.exclude(preLineAnnotationStreamParser.head);
        areaAnnotationEnd = BlankStreamParser.exclude(preAreaAnnotationStreamParser.head);
        stringBound = BlankStreamParser.exclude(preEscapedStringStreamParser.head);
    }

    @Override
    void resumePreParsers() {
        lineAnnotationHead = preLineAnnotationStreamParser.head[0];
        areaAnnotationEnd = preAreaAnnotationStreamParser.head[0];
        stringBound = preEscapedStringStreamParser.head[0];
    }

    static char exclude(char[] characters) {
        Set<Character> set = new HashSet<>();
        for (char character : characters) {
            set.add(character);
        }
        for (char character = 0 ; character < Character.MAX_VALUE ; ) {
            if (!set.contains(character)) {
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
    protected void endInput() {
        if (!builder.isEmpty()) {
            wordParser.input(builder.toString());
            builder.setLength(0);
        }
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
        if (character >= delimiters.length || !delimiters[character]) {
            builder.append(character);
        }
        else {
            if (!builder.isEmpty()) {
                wordParser.input(builder.toString());
                builder.setLength(0);
            }
            if (character >= blanks.length || !blanks[character]) {
                semanticRegex.input(character , wordParser.getType("Punctuation"));
            }
        }
    }
}

class PreLineStructureStreamParser extends InnerStreamParser {
    BlankStreamParser blankStreamParser;
    LineStructureStreamParser lineAnnotationStreamParser;
    int index;
    char[] head;

    @Override
    protected void endInput() {
        setStreamParser(blankStreamParser);
        blankStreamParser.hidePreParsers();
        for (int index = 0 ; index < this.index ; ) {
            blankStreamParser.input(head[index++]);
        }
        blankStreamParser.resumePreParsers();
        blankStreamParser.endInput();
    }

    protected PreLineStructureStreamParser(StructuredDocumentParser structuredDocumentParser , BlankStreamParser blankStreamParser) {
        super(structuredDocumentParser);
        this.blankStreamParser = blankStreamParser;
    }

    void hidePreParsers() {
        blankStreamParser.lineAnnotationHead = BlankStreamParser.exclude(head);
    }

    void resumePreParsers() {
        blankStreamParser.lineAnnotationHead = head[0];
    }

    @Override
    void input(char character) {
        if (character == head[index++]) {
            if (index == head.length) {
                setStreamParser(lineAnnotationStreamParser);
                blankStreamParser.endInput();
            }
        }
        else {
            setStreamParser(blankStreamParser);
            hidePreParsers();
            index--;
            for (int index = 0 ; index < this.index ; ) {
                blankStreamParser.input(head[index++]);
            }
            structuredDocumentParser.streamParser.input(character);
            resumePreParsers();
        }
    }
}

class LineStructureStreamParser extends InnerStreamParser {
    BlankStreamParser blankStreamParser;

    @Override
    protected void endInput() {
        blankStreamParser.endInput();
    }

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

    @Override
    protected void endInput() {
        setStreamParser(blankStreamParser);
        blankStreamParser.hidePreParsers();
        for (int index = 0 ; index < this.index ; ) {
            blankStreamParser.input(head[index++]);
        }
        blankStreamParser.resumePreParsers();
        blankStreamParser.endInput();
    }

    protected PreAreaStreamParser(StructuredDocumentParser structuredDocumentParser , BlankStreamParser blankStreamParser) {
        super(structuredDocumentParser , blankStreamParser);
        this.blankStreamParser = blankStreamParser;
    }

    @Override
    void hidePreParsers() {
        blankStreamParser.lineAnnotationHead = BlankStreamParser.exclude(blankStreamParser.preLineAnnotationStreamParser.head);
        blankStreamParser.areaAnnotationEnd = BlankStreamParser.exclude(head);
    }

    @Override
    void resumePreParsers() {
        blankStreamParser.lineAnnotationHead = blankStreamParser.preLineAnnotationStreamParser.head[0];
        blankStreamParser.areaAnnotationEnd = head[0];
    }

    @Override
    void input(char character) {
        if (index == head.length) {
            areaStreamParser.enter();
            setStreamParser(areaStreamParser);
            blankStreamParser.endInput();
            areaStreamParser.input(character);
            return;
        }
        if (character == head[index++]) {
            return;
        }
        setStreamParser(blankStreamParser);
        hidePreParsers();
        index--;
        for (int index = 0 ; index < this.index ; ) {
            blankStreamParser.input(head[index++]);
        }
        structuredDocumentParser.streamParser.input(character);
        resumePreParsers();
    }
}

abstract class AreaStreamParser extends LineStructureStreamParser {
    char end;
    AreaEndStreamParser areaEndStreamParser;

    protected void endArea() {
    }

    @Override
    protected void endInput() {
        throw new SDPException("SDPException-IncompleteDocument: Someone has invoked method:endInput() of StructuredDocumentParser, but the document parsing is yet to be complete.");
    }

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

    @Override
    protected void endInput() {
        if (index == end.length) {
            setStreamParser(blankStreamParser);
            areaStreamParser.endArea();
            return;
        }
        throw new SDPException("SDPException-IncompleteDocument: Someone has invoked method:endInput() of StructuredDocumentParser, but the document parsing is yet to be complete.");
    }

    protected AreaEndStreamParser(StructuredDocumentParser structuredDocumentParser , BlankStreamParser blankStreamParser , AreaStreamParser streamParser) {
        super(structuredDocumentParser);
        this.blankStreamParser = blankStreamParser;
        areaStreamParser = streamParser;
    }

    @Override
    void input(char character) {
        if (index == end.length) {
            setStreamParser(blankStreamParser);
            areaStreamParser.endArea();
            blankStreamParser.input(character);
            return;
        }
        if (character == end[index++]) {
        }
        else {
            areaStreamParser.end = BlankStreamParser.exclude(end);
            setStreamParser(areaStreamParser);
            index--;
            for (int index = 0 ; index < this.index ; ) {
                areaStreamParser.input(end[index++]);
            }
            areaStreamParser.input(character);
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

class PreEscapedStringStreamParser extends PreAreaStreamParser {
    protected PreEscapedStringStreamParser(StructuredDocumentParser structuredDocumentParser, BlankStreamParser blankStreamParser) {
        super(structuredDocumentParser, blankStreamParser);
    }

    @Override
    void hidePreParsers() {
        blankStreamParser.lineAnnotationHead = BlankStreamParser.exclude(blankStreamParser.preLineAnnotationStreamParser.head);
        blankStreamParser.areaAnnotationEnd = BlankStreamParser.exclude(head);
        blankStreamParser.stringBound = BlankStreamParser.exclude(head);
    }

    @Override
    void resumePreParsers() {
        blankStreamParser.lineAnnotationHead = blankStreamParser.preLineAnnotationStreamParser.head[0];
        blankStreamParser.areaAnnotationEnd = head[0];
        blankStreamParser.stringBound = head[0];
    }
}

class EscapedStringStreamParser extends AreaStreamParser {
    StringBuilder stringBuilder;
    EscapeStreamParser escapeStreamParser;
    char escape;

    EscapedStringStreamParser(StructuredDocumentParser structuredDocumentParser) {
        super(structuredDocumentParser);
        stringBuilder = new StringBuilder();
        escapeStreamParser = new EscapeStreamParser(structuredDocumentParser , stringBuilder , this);
    }

    @Override
    protected void endArea() {
        semanticRegex.input(stringBuilder.toString() , structuredDocumentParser.wordParser.getType("String"));
        stringBuilder.setLength(0);
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
            areaStreamParser.endArea();
            setStreamParser(blankStreamParser);
            blankStreamParser.input(character);
            return;
        }
        if (character == end[index++]) {
        }
        else {
            areaStreamParser.end = BlankStreamParser.exclude(end);
            setStreamParser(areaStreamParser);
            index--;
            for (int index = 0 ; index < this.index ; ) {
                areaStreamParser.input(end[index++]);
            }
            areaStreamParser.input(character);
            areaStreamParser.end = end[0];
        }
    }
}

class EscapeStreamParser extends InnerStreamParser {
    EscapedStringStreamParser escapedStringStreamParser;
    List<StreamParserStandardSystem.EscapeParser> escapeParsers;
    StreamParserStandardSystem.EscapeParser parser;
    StringBuilder builder;
    boolean normalizeUnknownEscape;

    @Override
    protected void endInput() {
        throw new SDPException("SDPException-IncompleteDocument: Someone has invoked method:endInput() of StructuredDocumentParser, but the document parsing is yet to be complete.");
    }

    EscapeStreamParser(StructuredDocumentParser structuredDocumentParser , StringBuilder stringBuilder , EscapedStringStreamParser escapeStreamParser) {
        super(structuredDocumentParser);
        this.escapedStringStreamParser = escapeStreamParser;
        escapeParsers = new ChunkChainList<>();
        builder = new StringBuilder();
        normalizeUnknownEscape = false;
    }

    @Override
    void input(char character) {
        if (parser == null) {
            builder.setLength(0);
            escapeParsers.iterateInterruptibly(escapeParser -> {
                StreamParserStandardSystem.EscapeParsingState status;
                if ((status = escapeParser.parse(character)) != StreamParserStandardSystem.EscapeParsingState.NOT_CORRESPOND) {
                    if (status == StreamParserStandardSystem.EscapeParsingState.COMPLETE) {
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
        StreamParserStandardSystem.EscapeParsingState status = parser.parse(character);
        if (status == StreamParserStandardSystem.EscapeParsingState.COMPLETE) {
            escapedStringStreamParser.stringBuilder.append(parser.getText());
            builder.setLength(0);
            parser.reset();
            parser = null;
            setStreamParser(escapedStringStreamParser);
        }
        else if (status == StreamParserStandardSystem.EscapeParsingState.INCOMPLETE) {
        }
        else if (status == StreamParserStandardSystem.EscapeParsingState.OVERFLOWN) {
            escapedStringStreamParser.stringBuilder.append(parser.getText());
            builder.setLength(0);
            parser.reset();
            parser = null;
            setStreamParser(escapedStringStreamParser);
            escapedStringStreamParser.input(character);
        }
        else {
            parser.reset();
            parser = null;
            setStreamParser(escapedStringStreamParser);
            builder.setLength(0);
            if (normalizeUnknownEscape) {
                escapedStringStreamParser.stringBuilder.append(escapedStringStreamParser.escape);
                escapedStringStreamParser.stringBuilder.append(builder);
                throw new SDPException("SDPException-UnknownEscapeFormat: \"" + escapedStringStreamParser.escape + builder.toString() + "\", and is already normalized and appended to the string.");
            }
            throw new SDPException("SDPException-UnknownEscapeFormat: \"" + escapedStringStreamParser.escape + builder.toString() + '"');
        }
    }
}
