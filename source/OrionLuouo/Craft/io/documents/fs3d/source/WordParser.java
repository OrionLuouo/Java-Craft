package OrionLuouo.Craft.io.documents.fs3d.source;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface WordParser {
    Set<Character> CHARACTERS_BLANK = new HashSet<>(Arrays.asList(' ', '\t', '\n', '\r'))
            ,CHARACTERS_PUNCTUATION = new HashSet<>(Arrays.asList(',' , '.' , '/' , '<' , '>' , '?' , ';' , ':' , '\'' , '@' , '[' , ']' , '{' , '}' , '#' , '~' , '!' , '"' , '$' ,'%' , '^' , '&' , '*' , '(' , ')' , '-' , '=' , '+' , '\\' , '|'));

    void input(char c);
}

class BlankParser implements WordParser {
    DocumentStatement documentStatement;
    StringBuilder builder;

    public BlankParser(DocumentStatement documentStatement) {
        this.documentStatement = documentStatement;
        builder = new StringBuilder();
    }

    public void input(char c) {
        if (CHARACTERS_BLANK.contains(c)) {
            if (!builder.isEmpty()) {
                documentStatement.grammarParser.input(builder.toString());
                builder = new StringBuilder();
            }
            if (c == '\n') {
                documentStatement.charIndex = 1;
                documentStatement.lineIndex++;
            }
        }
        else {
            if (CHARACTERS_PUNCTUATION.contains(c)) {
                if (!builder.isEmpty()) {
                    documentStatement.grammarParser.input(builder.toString());
                    builder = new StringBuilder();
                }
                documentStatement.grammarParser.input(c);
            }
            else {
                builder.append(c);
            }
            documentStatement.charIndex++;
        }
        documentStatement.charCount++;
    }
}

class LineAnnotationParser implements WordParser {
    static class MayEndLineAnnotationParser implements WordParser {
        DocumentStatement documentStatement;

        MayEndLineAnnotationParser(DocumentStatement documentStatement) {
            this.documentStatement = documentStatement;
        }

        @Override
        public void input(char c) {
            documentStatement.charCount++;
            if (c == '\n' || c == '\r') {
                documentStatement.charIndex = 1;
                documentStatement.lineIndex++;
                documentStatement.wordParser = new BlankParser(documentStatement);
                return;
            }
            documentStatement.charIndex++;
            if (c == '/') {
                documentStatement.wordParser = new BlankParser(documentStatement);
                return;
            }
            documentStatement.wordParser = new LineAnnotationParser(documentStatement);
        }
    }

    DocumentStatement documentStatement;

    LineAnnotationParser(DocumentStatement documentStatement) {
        this.documentStatement = documentStatement;
    }

    @Override
    public void input(char c) {
        documentStatement.charCount++;
        if (c == '\n' || c == '\r') {
            documentStatement.charIndex = 1;
            documentStatement.lineIndex++;
            documentStatement.wordParser = new BlankParser(documentStatement);
        }
        else {
            documentStatement.charIndex++;
        }
    }
}

class AreaAnnotationParser implements WordParser {
    static class MayEndAreaAnnotationParser implements WordParser {
        DocumentStatement documentStatement;

        MayEndAreaAnnotationParser(DocumentStatement documentStatement) {
            this.documentStatement = documentStatement;
        }

        @Override
        public void input(char c) {
            documentStatement.charCount++;
            if (c == '/') {
                documentStatement.charIndex++;
                documentStatement.wordParser = new BlankParser(documentStatement);
                return;
            }
            if (c == '\n' || c == '\r') {
                documentStatement.charIndex = 1;
                documentStatement.lineIndex++;
            }
            else {
                documentStatement.charIndex++;
            }
            documentStatement.wordParser = new AreaAnnotationParser(documentStatement);
        }
    }

    DocumentStatement documentStatement;

    AreaAnnotationParser(DocumentStatement documentStatement) {
        this.documentStatement = documentStatement;
    }

    @Override
    public void input(char c) {
        documentStatement.charCount++;
        if (c == '\n' || c == '\r') {
            documentStatement.charIndex = 1;
            documentStatement.lineIndex++;
        }
        else {
            documentStatement.charIndex++;
            if (c == '*') {
                documentStatement.wordParser = new MayEndAreaAnnotationParser(documentStatement);
            }
        }
    }
}