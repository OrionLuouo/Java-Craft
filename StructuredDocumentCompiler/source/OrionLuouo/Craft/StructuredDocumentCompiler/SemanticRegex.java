package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.IncompleteRegexException;
import OrionLuouo.Craft.StructuredDocumentCompiler.exception.SemanticMismatchException;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.Stack;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class SemanticRegex extends SemanticUnit {
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
        switch (match.state) {
            case STATE_COMPLETE -> {
                /* Maybe this is of no use.
                if (match.regexIndex != -1) {
                    match.uncertainMatchCount++;
                    match.uncertainMatchedUnits.add(unitNow);
                }
                if (unitNow.regexIndex != -1) {
                    boolean potential = false;
                    Iterator<SemanticUnit> iterator = unitNow.children.iterator();
                    while (iterator.hasNext()) {
                        potential |= iterator.next().isPotential();
                    }
                    if (potential) {
                        match.uncertainMatchedUnits.clean();
                        match.regexIndex = unitNow.regexIndex;
                        match.uncertainMatchCount = 0;
                    }
                    else {
                        compiler.structureLayer.parseSemantics(match , unitNow.regexIndex);
                    }
                }
                 */
                if (unitNow.regexIndex != -1 && unitNow.children.isEmpty()) {
                    compiler.structureLayer.parseSemantics(match , unitNow.regexIndex);
                }
            }
            case STATE_INCOMPLETE -> {
            }
            case STATE_MISMATCH -> {
                /*
                /*
                To catch up the mismatched content,
                and rematches them with the new regex.
                The latest one word/punctuation will be makeup by method:matches(),
                not using MakeupMatchUnit.
                * /
                match.uncertainMatchCount++;
                if (match.uncertainMatchedUnits.size() == 0) {
                    throw new SemanticMismatchException("Document contents not corresponding to the regex, QAQ!");
                }
                else {
                    Stack<SemanticMatchUnit.MakeupUnit> makeupUnitList = new ChunkChainList<>();
                    while (match.uncertainMatchCount-- > 0) {
                        makeupUnitList.add(match.uncertainMatchedUnits.pop().makeup(match.matchUnits.pop()));
                    }
                    compiler.structureLayer.parseSemantics(match , unitNow.regexIndex);
                    while (makeupUnitList.size() != 0) {
                        makeupUnitList.pop().input(compiler.semanticRegex);
                    }
                }
                return false;
                */
                throw new SemanticMismatchException("Document contents not corresponding to the regex!QAQ!");
            }
            default -> {
                throw new SDCException("SDCException-UnexpectedRuntimeError: Unknown state occurred in the SemanticRegex compiling process.");
            }
        }
        return true;
    }

    void matches(char punctuation) {
        if (unitNow == null) {
            for (SemanticUnit unit : roots) {
                unit.matches(match , punctuation);
                if (match.state != SemanticMatch.MatchState.STATE_MISMATCH) {
                    unitNow = unit;
                    break;
                }
            }
            return;
        }
        if (match.state == SemanticMatch.MatchState.STATE_COMPLETE) {
            if (unitNow.children.isEmpty()) {
                throw new IncompleteRegexException("QAQ!");
            }
            Iterator<SemanticUnit> iterator = unitNow.children.iterator();
            while (iterator.hasNext()) {
                SemanticUnit unit = iterator.next();
                unit.matches(match , punctuation);
                if (match.state != SemanticMatch.MatchState.STATE_MISMATCH) {
                    unitNow = unit;
                    if(!checkMatch()) {
                        compiler.semanticRegex.matches(punctuation);
                        return;
                    }
                    unitNow = unit;
                    return;
                }
            }
            throw new SemanticMismatchException("QAQ!");
        }
        else {
            unitNow.matches(match , punctuation);
            if(!checkMatch()) {
                compiler.semanticRegex.matches(punctuation);
                return;
            }
        }
    }

    void matches(Object object , GrammarParser.WordType type) {
        if (unitNow == null) {
            for (SemanticUnit unit : roots) {
                unit.matches(match , object , type);
                if (match.state != SemanticMatch.MatchState.STATE_MISMATCH) {
                    unitNow = unit;
                    break;
                }
            }
            return;
        }
        if (match.state == SemanticMatch.MatchState.STATE_COMPLETE) {
            if (unitNow.children.isEmpty()) {
                throw new IncompleteRegexException("QAQ!");
            }
            Iterator<SemanticUnit> iterator = unitNow.children.iterator();
            while (iterator.hasNext()) {
                SemanticUnit unit = iterator.next();
                unit.matches(match , object , type);
                if (match.state != SemanticMatch.MatchState.STATE_MISMATCH) {
                    unitNow = unit;
                    if(!checkMatch()) {
                        compiler.semanticRegex.matches(object , type);
                        return;
                    }
                    unitNow = unit;
                    return;
                }
            }
            throw new SemanticMismatchException("QAQ!");
        }
        else {
            unitNow.matches(match , object , type);
            if(!checkMatch()) {
                compiler.semanticRegex.matches(object , type);
                return;
            }
        }
    }

    @Override
    void matches(SemanticMatch match, Object object, GrammarParser.WordType type) {
    }

    @Override
    void matches(SemanticMatch match , char punctuation) {

    }
}