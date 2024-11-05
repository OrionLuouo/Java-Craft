package OrionLuouo.Craft.StructuredDocumentCompiler;


/**
 * The first level in the compilers chain.
 * To accept the character-type raw data and conclude them into words.
 */
public interface WordParser {
    char[] CHARACTERS_BLANK = new char[]{' ', '\t', '\n', '\r', '\f'}
            , CHARACTERS_PUNCTUATION = new char[]{'!' , '"' , '$' , '\'' , '%' , '^' , '&' , '*' , '(' , ')' , '-' , '+' , '=' , '[' , ']' , '{' , '}' , '#' , '~' , ';' , ':' , ',' , '.' , '<' , '>' , '/' , '?'};

    long[] INDEXES_BLANK = compileIndex(CHARACTERS_BLANK)
            , INDEXES_DELIMITER = compileIndex(CHARACTERS_BLANK , CHARACTERS_PUNCTUATION);

    static long[] compileIndex(char[]... characterSets) {
        long[] indexes = new long[2];
        for (char[] set : characterSets) {
            for (char character : set) {
                indexes[(character & 0x0040) >> 6] |= 1L << (character & 0x003F);
            }
        }
        return indexes;
    }

    static boolean contains(long[] indexes, char character) {
        return (indexes[(character & 0x0040) >> 6] & (1L << (character & 0x003F))) != 0;
    }

    void accept(char character);
}

class BlankWordParser implements WordParser {
    StringBuilder builder;
    GrammarParser grammarParser;

    BlankWordParser() {
        builder = new StringBuilder();
    }

    @Override
    public void accept(char character) {
        if (WordParser.contains(WordParser.INDEXES_DELIMITER, character)) {
            if (WordParser.contains(WordParser.INDEXES_BLANK, character)) {
                if (!builder.isEmpty()) {
                    grammarParser.word(builder.toString());
                    builder = new StringBuilder();
                }
                return;
            }
            if (!builder.isEmpty()) {
                grammarParser.word(builder.toString());
                builder = new StringBuilder();
            }
            grammarParser.punctuation(character);
        }
    }
}

class LineAnnotationWordParser extends BlankWordParser {

    @Override
    public void accept(char character) {
        if (character == '\n' || character == '\r') {
            documentStatement.wordParser = new BlankWordParser(documentStatement);
        }
    }
}

class AreaAnnotationWordParser extends BlankWordParser {
    AreaAnnotationWordParser(DocumentStatement documentStatement) {
        super(documentStatement);
    }

    @Override
    public void accept(char character) {
        if (character == '*') {
            documentStatement.wordParser = new PreAreaAnnotationEndWordParser(documentStatement);
        }
    }
}

class PreAreaAnnotationEndWordParser extends BlankWordParser {
    PreAreaAnnotationEndWordParser(DocumentStatement documentStatement) {
        super(documentStatement);
    }

    @Override
    public void accept(char character) {
        documentStatement.wordParser = character == '/' ? new BlankWordParser(documentStatement) : new AreaAnnotationWordParser(documentStatement);
    }
}