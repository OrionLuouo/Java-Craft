package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.Stack;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.io.IOException;
import java.io.Reader;
import java.util.stream.Stream;

public class Compiler {
    WordParser wordParser;
    GrammarParser grammarParser;
    SemanticRegex semanticRegex;
    StructureLayer structureLayer;
    Stack<StructureLayer> layerStack;

    public Compiler() {
        wordParser = new BlankWordParser(this);
        grammarParser = new GrammarParser(this);
        layerStack = new ChunkChainList<>(16);
    }

    /**
     * To input the document's content to the compiler.
     *
     * @throws SDCException All the exceptions occurred in the compiling process will be thrown here,
     *                      and the invoker should catch and handle them properly.
     */
    public void input(String text) throws SDCException {
        for (char character : text.toCharArray()) {
            wordParser.input(character);
        }
    }

    /**
     * To input the document's content to the compiler.
     *
     * @throws SDCException All the exceptions occurred in the compiling process will be thrown here,
     *                      and the invoker should catch and handle them properly.
     */
    public void input(Stream<Character> stream) {
        stream.forEach(wordParser::input);
    }

    /**
     * To input the document's content to the compiler.
     *
     * @throws SDCException All the exceptions occurred in the compiling process will be thrown here,
     *                      and the invoker should catch and handle them properly.
     */
    public void input(Reader reader) throws SDCException , IOException {
        char[] buffer = new char[1024];
        int count;
        while ((count = reader.read(buffer)) != -1) {
            for (int i = 0; i < count; ) {
                wordParser.input(buffer[i++]);
            }
        }
    }

    /**
     * To end the document's compiling.
     * Actually,
     * this method will not deconstruct the objects that do compile work.
     * It only informs the compiler that the document should have come to a position that can be an end point.
     *
     * @throws SDCException If the sentence before can't be compiled properly,
     *                      the exception being thrown out,
     *                      of course.
     *                      But,
     *                      when the document "may" end,
     *                      however the real content is not splittable here,
     *                      an exception occurred,
     *                      too.
     */
    @Unfinished
    public void end() throws SDCException {
        wordParser.input(' ');
    }

    /**
     * To jump out of the current layer,
     * and be back to its outer layer.
     * The current one will be logout.
     */
    public void retractLayer() {
        structureLayer.logout(this);
        (structureLayer = layerStack.pop()).reload(this);
    }

    /**
     * To replace the current StructureLayer with a new one,
     * and the compiler not keeping the former.
     * Especially,
     * you should provide a layer by this method,
     * while initializing the Compiler.
     *
     * @param layer The new layer to be used.
     *              It should be on the same level with the current one.
     */
    public void coverLayer(StructureLayer layer) {
        structureLayer.logout(this);
        (structureLayer = layer).reload(this);
    }

    /**
     * To overlap on the current StructureLayer.
     * The compiler will keep the overlapped in a layer stack,
     * and release when the newly overlapping one is retracted.
     *
     * @param layer The new layer to be used.
     *              It should be on the lower level than the current layer.
     */
    public void overlapLayer(StructureLayer layer) {
        layerStack.add(layer);
        (structureLayer = layer).reload(this);
    }

    public void setSemanticRegex(SemanticRegex semanticRegex) {
        this.semanticRegex = semanticRegex;
    }

    public GrammarParser getGrammarParser() {
        return grammarParser;
    }
}
