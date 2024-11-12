package OrionLuouo.Craft.StructuredDocumentCompiler;

import java.util.ArrayList;
import java.util.List;

public abstract class SemanticUnit {
    List<SemanticUnit> children;
    int regexIndex;

    SemanticUnit() {
        children = new ArrayList<>();
        regexIndex = -1;
    }

    void matches(SemanticMatch match , char punctuation) {
        match.state = SemanticMatch.MatchState.STATE_MISMATCH;
    }

    abstract void matches(SemanticMatch match , Object object , GrammarParser.WordType type);

    /*
    boolean isPotential() {
        return false;
    }
     */

    //abstract SemanticMatchUnit.MakeupUnit makeup(SemanticMatchUnit matchUnit);

    boolean isPunctuation() {
        return false;
    }
}

class PunctuationSemanticUnit extends SemanticUnit {
    char punctuation;

    PunctuationSemanticUnit(char punctuation) {
        this.punctuation = punctuation;
    }

    @Override
    void matches(SemanticMatch match, char punctuation) {
        match.state = punctuation == this.punctuation ? SemanticMatch.MatchState.STATE_COMPLETE : SemanticMatch.MatchState.STATE_MISMATCH;
    }

    @Override
    void matches(SemanticMatch match, Object object, GrammarParser.WordType type) {
        match.state = SemanticMatch.MatchState.STATE_MISMATCH;
    }

    /*
    @Override
    SemanticMatchUnit.MakeupUnit makeup(SemanticMatchUnit matchUnit) {
        return new SemanticMatchUnit.MakeupUnit.PunctuationMakeupUnit(punctuation);
    }
     */
}

class ElementSemanticUnit extends SemanticUnit {
    GrammarParser.WordType type;

    @Override
    void matches(SemanticMatch match, Object object, GrammarParser.WordType type) {
        if (this.type == type) {
            match.matchUnits.add(new ElementSemanticMatchUnit(object));
        }
        else {
            match.state = SemanticMatch.MatchState.STATE_MISMATCH;
        }
    }

    /*
    @Override
    SemanticMatchUnit.MakeupUnit makeup(SemanticMatchUnit matchUnit) {
        return regex -> {
            regex.matches(((ElementSemanticMatchUnit) matchUnit).value , type);
        };
    }
     */
}

class ArbitraryElementSemanticUnit extends SemanticUnit {
    GrammarParser.WordType type;

    @Override
    void matches(SemanticMatch match, Object object, GrammarParser.WordType type) {
        if (this.type == type) {
            ((ArbitraryElementSemanticMatchUnit) match.matchUnits.getLast()).values.add(object);
            match.state = SemanticMatch.MatchState.STATE_INCOMPLETE;
        }
        else {
            match.state = SemanticMatch.MatchState.STATE_INCOMPLETE;
        }
    }
}

class ArbitraryPunctuationSemanticUnit extends PunctuationSemanticUnit {
    ArbitraryPunctuationSemanticUnit(char punctuation) {
        super(punctuation);
    }

    @Override
    public void matches(SemanticMatch match , char punctuation) {
        if (punctuation == this.punctuation) {
            match.state = SemanticMatch.MatchState.STATE_INCOMPLETE;
        }
        else {
            match.state = SemanticMatch.MatchState.STATE_MISMATCH;
        }
    }
}

class MergeSemanticUnit extends SemanticRegex {
    SemanticMatchUnit matchUnit;

    MergeSemanticUnit(Compiler compiler) {
        super(null);
        compiler = new Compiler() {
            {
                structureLayer = new StructureLayer() {
                    @Override
                    public void reload(Compiler compiler) {
                    }

                    @Override
                    public void parseSemantics(SemanticMatch match, int semanticIndex) {
                        matchUnit = match;
                    }

                    @Override
                    public void logout(Compiler compiler) {
                    }
                };
            }
        };
    }

    @Override
    void matches(SemanticMatch match, Object object, GrammarParser.WordType type) {
        matches(object , type);
        if (matchUnit != null) {
            match.matchUnits.add(matchUnit);
        }
    }

    @Override
    void matches(SemanticMatch match , char punctuation) {
        matches(punctuation);
        if (matchUnit != null) {
            match.matchUnits.add(matchUnit);
        }
    }
}

class ArbitraryMergeSemanticUnit extends MergeSemanticUnit {
    ArbitraryMergeSemanticMatchUnit matchUnit;

    ArbitraryMergeSemanticUnit(Compiler compiler) {
        super(compiler);
        matchUnit = new ArbitraryMergeSemanticMatchUnit();
    }

    void
}