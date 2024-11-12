package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.SemanticMismatchException;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.sequence.*;

import java.util.LinkedList;

public class SemanticMatch extends SemanticMatchUnit {
    enum MatchState {
        STATE_INCOMPLETE , STATE_COMPLETE , STATE_MISMATCH;
    }

    List<SemanticMatchUnit> matchUnits;
    int regexIndex /*, uncertainMatchCount*/;
    //Stack<SemanticUnit> uncertainMatchedUnits;
    MatchState state;

    SemanticMatch() {
        matchUnits = new ChunkChainList<>();
        regexIndex = -1;
        //uncertainMatchCount = 0;
        //uncertainMatchedUnits = new CycledArrayList<>();
    }

    public Iterator<SemanticMatchUnit> iterator() {
        return matchUnits.iterator();
    }
}