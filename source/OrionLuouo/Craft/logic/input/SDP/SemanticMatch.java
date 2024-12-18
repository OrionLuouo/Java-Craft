package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.Iterable;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.system.annotation.Unfinished;

import javax.swing.text.Element;

public class SemanticMatch{
    public static final StateLayer ROOT_STATE_LAYER = abandoned -> {
        throw new SDPException("SDPException-Mismatched: The regex mismatched the input.");
    };

    StateLayer branch;
    int branchCount;
    List<CouplePair<Object , WordParser.WordType>> record;
    List<MatchUnit> matchUnits;

    SemanticMatch() {
        branch = ROOT_STATE_LAYER;
        record = new ChunkChainList<>();
        matchUnits = new ChunkChainList<>();
    }

    void rollback() {
        matchUnits.remove(matchUnits.size() - branchCount , branchCount);
        branchCount = 0;
    }
}