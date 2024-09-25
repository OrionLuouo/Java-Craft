package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.io.documents.fs3d.FS3Document;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.Statement;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class DocumentStatement extends FS3Document implements Statement {
    WordParser wordParser;
    GrammarParser grammarParser;
    int lineIndex , charIndex , charCount;
    @Unfinished(state = "Unimplemented")
    StringBuilder sentenceNow;
    Object wordNow;
    String stateNow;
    LinkedList<AreaLayer> layersStack;
    AreaLayer currentAreaLayer;
    StateLayer currentLayer;
    Map<String , Variable> variables;
    Map<String , TypeStatement> types;
    TypeStatement rootTypeState;
    TypeStatement thisType;
    Map<FS3DType , TypeStatement> typeStatementMap;

    public DocumentStatement() {
        super();
        rootTypeState = ((FinalTypeStatement) (root = new FinalTypeStatement() {
            @Override
            public boolean isParentOf(FS3DType type) {
                return false;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public FS3DType[] getParents() {
                return new FS3DType[0];
            }

            @Override
            public FS3DType[] getInternalTypes() {
                return new FS3DType[0];
            }

            @Override
            public int areaSize() {
                return 0;
            }

            @Override
            public Iterator<FS3DObject> areaIterator() {
                return null;
            }

            @Override
            public FS3DType getArrayType() {
                return null;
            }

            @Override
            public FS3DType getElementType() {
                return null;
            }

            @Override
            public boolean isArrayType() {
                return false;
            }

            @Override
            public boolean isBasicType() {
                return false;
            }

            @Override
            public int getFieldIndex(String name) {
                return 0;
            }

            @Override
            public Iterator<CouplePair<String, FS3DType>> getFieldIterator() {
                return null;
            }
        }));
        wordParser = new BlankParser(this);
        grammarParser = new GrammarParser(this);
        lineIndex = 1;
        charIndex = 1;
        charCount = 0;
        sentenceNow = new StringBuilder();
        wordNow = "";
        stateNow = "";
        layersStack = new LinkedList<>();
        currentLayer = currentAreaLayer = new TypeAreaLayer(this , rootTypeState);
        variables = new HashMap<>();
        types = new HashMap<>();
        thisType = rootTypeState;
        typeStatementMap = new HashMap<>();
    }

    protected void retractLayer() {
        currentAreaLayer.logout();
        currentLayer = currentAreaLayer = layersStack.pop();
        currentAreaLayer.reload();
    }

    protected void coverLayer(AreaLayer layer) {
        layersStack.push(currentAreaLayer);
        currentLayer = currentAreaLayer = layer;
    }

    protected void newLayer(StateLayer layer) {
        if (currentAreaLayer == currentLayer) {
            layersStack.push(currentAreaLayer);
        }
        currentLayer = layer;
    }

    protected void replaceLayer(AreaLayer layer) {
        if (currentAreaLayer == currentLayer) {
            currentAreaLayer = layer;
        }
        currentLayer = layer;
    }

    public void input(char c) {
        wordParser.input(c);
    }

    @Unfinished
    public boolean canBeSummarized() {
        return false;
    }

    @Override
    public String getWord() {
        return wordNow.toString();
    }

    @Override
    public String getSentence() {
        return sentenceNow.toString();
    }

    @Override
    public String getLocation() {
        return "Line= " + lineIndex + " Char Index= " + charIndex + " Char Count in Total= " + charCount;
    }

    @Override
    public String getState() {
        return stateNow;
    }
}
