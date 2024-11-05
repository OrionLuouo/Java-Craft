package OrionLuouo.Craft.StructuredDocumentCompiler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public interface WordParser {
    void input(char character) throws SDCException;
}

class BlankWordParser implements WordParser {
    static final Collection<Character> CHARACTER_BLANKS = Arrays.asList(' ' , '\r' , '\n' , '\t')
            , CHARACTER_PUNCTUATIONS = Arrays.asList('¬' , '~' , '`' , '!' , '"' , '£' , '$' , '%' , '^' , '&' , '*' , '(' , ')' , '-' , '_' , '=' , '+' , '[' , ']' , '{' , '}' , ';' , ':' , '\'' , '@' , ',' , '<' , '.' , '>' , '/' , '?' , '|' , '\\');
    static final long[] INDEX_DELIMITERS = compileIndex(CHARACTER_BLANKS , CHARACTER_PUNCTUATIONS)
            , INDEX_BLANKS = compileIndex(CHARACTER_BLANKS);

    @SafeVarargs
    static long[] compileIndex(Collection<Character>... characters) {
        long[] index = new long[2];
        for (Collection<Character> collection : characters) {
            Iterator<Character> iterator = collection.iterator();
            while (iterator.hasNext()) {
                char character = iterator.next();
                if (character > 127) {
                    throw new RuntimeException("SDCException-UnexpectedRuntimeError: Character's code out of range 127 while compiling WordParser's indexes.");
                }
                index[(character & 0x40) == 0 ? 0 : 1] |= 1L << (character & 0x3F);
            }
        }
        return index;
    }

    Compiler compiler;
    StringBuilder builder = new StringBuilder();

    BlankWordParser(Compiler compiler) {
        this.compiler = compiler;
        builder = new StringBuilder();
    }

    @Override
    public void input(char character) throws SDCException {
        if (((INDEX_DELIMITERS[(character & 0x40) == 0 ? 0 : 1] >> character & 0x3F) & 1) != 0) {
            if (((INDEX_BLANKS[(character & 0x40) == 0 ? 0 : 1] >> character & 0x3F) & 1) != 0) {
                if (!builder.isEmpty()) {
                    compiler.grammarParser.word(builder.toString());
                    builder.setLength(0);
                }
                return;
            }
            if (!builder.isEmpty()) {
                compiler.grammarParser.word(builder.toString());
                builder.setLength(0);
            }
            if (character == '/') {
                compiler.wordParser = new PreAnnotationWordParser(compiler , this);
                return;
            }
            compiler.semanticRegex.matches(character);
            return;
        }
        builder.append(character);
    }
}

class PreAnnotationWordParser extends BlankWordParser {
    BlankWordParser blankWordParser;

    PreAnnotationWordParser(Compiler compiler , BlankWordParser blankWordParser) {
        super(compiler);
        this.blankWordParser = blankWordParser;
    }

    @Override
    public void input(char character) {
        if (character == '/') {
            compiler.wordParser = new LineAnnotationWordParser(compiler , this);
            return;
        }
        if (character == '*') {
            compiler.wordParser = new AreaAnnotationWordParser(compiler , this);
            return;
        }
        compiler.semanticRegex.matches('/');
        compiler.wordParser = blankWordParser;
    }
}

class LineAnnotationWordParser extends PreAnnotationWordParser {

    LineAnnotationWordParser(Compiler compiler, BlankWordParser blankWordParser) {
        super(compiler, blankWordParser);
    }

    @Override
    public void input(char character) {
        if (character == 'r' || character == 'n') {
            compiler.wordParser = blankWordParser;
            return;
        }
    }
}

class AreaAnnotationWordParser extends PreAnnotationWordParser {

    AreaAnnotationWordParser(Compiler compiler, BlankWordParser blankWordParser) {
        super(compiler, blankWordParser);
    }

    @Override
    public void input(char character) {
        if (character == '*') {
            compiler.wordParser = new PreAreaAnnotationEndWordParser(compiler , this);
            return;
        }
    }
}

class PreAreaAnnotationEndWordParser extends AreaAnnotationWordParser {
    PreAreaAnnotationEndWordParser(Compiler compiler, BlankWordParser blankWordParser) {
        super(compiler, blankWordParser);
    }

    @Override
    public void input(char character) {
        compiler.wordParser = character == '/' ? blankWordParser : new AreaAnnotationWordParser(compiler, blankWordParser);
    }
}