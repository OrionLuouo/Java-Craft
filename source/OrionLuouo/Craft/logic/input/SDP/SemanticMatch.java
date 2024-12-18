package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.Iterable;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.system.annotation.Unfinished;

import javax.swing.text.Element;

public class SemanticMatch implements Iterable<MatchUnit> {
    SemanticUnit branchNode;
    int branchIndex;
    List<CouplePair<Object , WordParser.WordType>> record;
    List<MatchUnit> matchUnits;

    @Override
    public Iterator<MatchUnit> iterator() {
        return matchUnits.iterator();
    }
}

class MatchRecord extends ChunkChainList<CouplePair<Object , WordParser.WordType>> {
    SemanticUnit branchNode;
    int branchIndex;

    MatchRecord(SemanticUnit branchNode , int branchIndex) {
        super(8);
        this.branchNode = branchNode;
        this.branchIndex = branchIndex;
    }
}