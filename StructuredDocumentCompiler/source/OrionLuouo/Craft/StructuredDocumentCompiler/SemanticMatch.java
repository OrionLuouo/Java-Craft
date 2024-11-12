package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.SemanticMismatchException;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.sequence.*;

import java.util.LinkedList;

public class SemanticMatch extends SemanticMatchUnit {
    enum MatchState {
    }

    List<SemanticMatchUnit> matchUnits;
    int regexIndex;
    MatchState state;

    SemanticMatch() {
        matchUnits = new ChunkChainList<>();
        regexIndex = -1;
    }

    public Iterator<SemanticMatchUnit> iterator() {
        return matchUnits.iterator();
    }
}