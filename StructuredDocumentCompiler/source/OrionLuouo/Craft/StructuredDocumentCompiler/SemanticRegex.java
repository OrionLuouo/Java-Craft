package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.IncompleteRegexException;
import OrionLuouo.Craft.StructuredDocumentCompiler.exception.SemanticMismatchException;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.Stack;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class SemanticRegex {
    Compiler compiler;
    SemanticUnit[] roots , leaves;
    SemanticUnit unitNow;
    SemanticMatch match;

    private SemanticRegex() {
    }

    SemanticRegex(Compiler compiler) {
        this.compiler = compiler;
    }

    @Unfinished
    public static SemanticRegex compile(Compiler compiler , String regex) throws SDCException {
        Compiler simpleCompiler = compiler;
        SemanticUnit superRoot = new PunctuationSemanticUnit((char) 0);
        final SemanticUnit[][] units = {new SemanticUnit[]{superRoot}};
        simpleCompiler.grammarParser = new GrammarParser(simpleCompiler) {
            @Override
            public void word(String word) {

            }
        };
        simpleCompiler.semanticRegex = new SemanticRegex() {
            @Override
            public void matches(char punctuation) {
                SemanticUnit newUnit = null;
                switch (punctuation) {
                    case '/' -> {

                    }
                    case '(' -> {

                    }
                    case ')' -> {

                    }
                    case '|' -> {

                    }
                    case '?' -> {

                    }
                    case '*' -> {

                    }
                    case '+' -> {

                    }
                    default -> {
                        newUnit = new PunctuationSemanticUnit(punctuation);
                    }
                }
                for (SemanticUnit unit : units[0]) {
                    unit.children.add(newUnit);
                }
                units[0] = new SemanticUnit[] {newUnit};
            }
        };
        simpleCompiler.input(regex);
        SemanticRegex semanticRegex = new SemanticRegex();
        semanticRegex.roots = superRoot.children.toArray(new SemanticUnit[superRoot.children.size()]);
        semanticRegex.leaves = units[0];
        semanticRegex.compiler = compiler;
        return semanticRegex;
    }

    @Unfinished
    public static SemanticRegex compile(Compiler compiler , String regex, String... units) throws SDCException {

        return null;
    }

    /**
     * To paste the regex to this one.
     * The two objects will be simply merged,
     * referring to each other.
     * So if a regex is needed in multiple conditions,
     * it would be better to compile one for each.
     * And be attention that,
     * the original object will keep its state,
     * meaning you can't paste any suffix of variable:regex to this object.
     * But it's valid to use this in registration to the Compiler.
     */
    public SemanticRegex bifurcate(SemanticRegex regex) {
        Collection<SemanticUnit> collection = Arrays.asList(regex.roots);
        for (SemanticUnit unit : leaves) {
            unit.children.addAll(collection);
        }
        SemanticRegex newRegex = new SemanticRegex();
        newRegex.roots = roots;
        newRegex.leaves = regex.leaves;
        return newRegex;
    }

    public void addSemantics(int regexIndex) {
        for (SemanticUnit unit : leaves) {
            unit.regexIndex = regexIndex;
        }
    }

    void reset() {
        unitNow = null;
        match = new SemanticMatch();
        match.state = SemanticMatch.MatchState.STATE_COMPLETE;
    }

    final boolean checkMatch() {
        return false;
    }

    void matches(char punctuation) {
    }

    void matches(Object object , GrammarParser.WordType type) {
    }
}