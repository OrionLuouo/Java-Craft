package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.sequence.*;

import java.util.LinkedList;

public class SemanticMatch {
    enum MatchState {
        COMPLETE , INCOMPLETE , POTENTIAL , MISMATCH , POTENTIAL_MISMATCH;
    }

    List<SemanticMatchUnit> matchUnits;
    int regexIndex , uncertainUnitCount;
    MatchState state;

    SemanticMatch() {
        matchUnits = new ChunkChainList<>();
        regexIndex = -1;
    }

    public Iterator<SemanticMatchUnit> iterator() {
        return matchUnits.iterator();
    }
}