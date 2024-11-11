package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.SemanticMismatchException;

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

    }

    abstract void matches(SemanticMatch match , Object object , GrammarParser.WordType type);

    boolean isPotential() {
        return false;
    }

    abstract SemanticMatchUnit.MakeupUnit makeup(SemanticMatchUnit matchUnit);

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

    @Override
    SemanticMatchUnit.MakeupUnit makeup(SemanticMatchUnit matchUnit) {
        return new SemanticMatchUnit.MakeupUnit.PunctuationMakeupUnit(punctuation);
    }
}

class ElementSemanticUnit extends SemanticUnit {
    GrammarParser.WordType type;

    @Override
    void matches(SemanticMatch match, Object object, GrammarParser.WordType type) {
        if (this.type == type) {

        }
        else {
            throw new SemanticMismatchException("An element of type " + this.type.name() + " is needed, yet one of type " + type.name() + " being provided.");
        }
    }

    @Override
    SemanticMatchUnit.MakeupUnit makeup(SemanticMatchUnit matchUnit) {
    }
}