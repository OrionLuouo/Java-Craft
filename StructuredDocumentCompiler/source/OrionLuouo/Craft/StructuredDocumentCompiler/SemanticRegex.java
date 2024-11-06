package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.IncompleteRegexException;
import OrionLuouo.Craft.StructuredDocumentCompiler.exception.SemanticMismatchException;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.data.container.collection.sequence.Stack;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.Iterator;

public class SemanticRegex {
    Compiler compiler;
    SemanticUnit[] roots , leaves;
    SemanticUnit unitNow;
    SemanticMatch match;

    SemanticRegex(Compiler compiler) {
        this.compiler = compiler;
    }

    @Unfinished
    public static SemanticRegex compile(Compiler compiler , String regex) throws SDCException {
        return null;
    }

    @Unfinished
    public static SemanticRegex compile(Compiler compiler , String regex, String... units) throws SDCException {

        return null;
    }

    @Unfinished
    public SemanticRegex bifurcate(SemanticRegex regex) {

        return regex;
    }

    @Unfinished
    public void addSemantics(int regexIndex) {

    }

    void reset() {
        unitNow = null;
        match = new SemanticMatch();
        match.state = SemanticMatch.MatchState.STATE_COMPLETE;
    }

    final void checkMatch() {
        switch (match.state) {
            case STATE_COMPLETE -> {
                match.uncertainMatchCount++;
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
                match.uncertainMatchedUnits.add(unitNow);
            }
            case STATE_INCOMPLETE -> {
            }
            case STATE_MISMATCH -> {
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
            }
            default -> {
                throw new SDCException("SDCException-UnexpectedRuntimeError: Unknown state occurred in the SemanticRegex compiling process.");
            }
        }
    }

    void matches(char punctuation) {
        if (match.state == SemanticMatch.MatchState.STATE_COMPLETE) {
            if (unitNow.children.isEmpty()) {
                throw new IncompleteRegexException("QAQ!");
            }
            Iterator<SemanticUnit> iterator = unitNow.children.iterator();
            while (iterator.hasNext()) {
                SemanticUnit unit = iterator.next();
                unit.matches(match , punctuation);
                if (match.state != SemanticMatch.MatchState.STATE_COMPLETE) {
                    checkMatch();
                    return;
                }
            }
            throw new SemanticMismatchException("QAQ!");
        }
        else {
            unitNow.matches(match , punctuation);
            checkMatch();
        }
    }

    void matches(Object object , GrammarParser.WordType type) {
        if (match.state == SemanticMatch.MatchState.STATE_COMPLETE) {
            if (unitNow.children.isEmpty()) {
                throw new IncompleteRegexException("QAQ!");
            }
            Iterator<SemanticUnit> iterator = unitNow.children.iterator();
            while (iterator.hasNext()) {
                SemanticUnit unit = iterator.next();
                unit.matches(match , object , type);
                if (match.state != SemanticMatch.MatchState.STATE_COMPLETE) {
                    checkMatch();
                    return;
                }
            }
            throw new SemanticMismatchException("QAQ!");
        }
        else {
            unitNow.matches(match , object , type);
            checkMatch();
        }
    }
}
