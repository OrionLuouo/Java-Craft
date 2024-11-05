package OrionLuouo.Craft.StructuredDocumentCompiler;

public interface StructureLayer {
    /**
     * To inform the StructureLayer that it is back into use.
     * If this layer may contain some special structured sentences,
     * in other words,
     * the layer needs a particularly designed SemanticRegex,
     * then you'd better set it to the Compiler every reloading.
     * The method will also be used to initializing a layer,
     * meaning it is invoked when the layer is provided to the Compiler,
     * by coverLayer(StructureLayer) or others.
     *
     * @param compiler The Compiler it is in.
     */
    void reload(Compiler compiler);
    void parseSemantics(SemanticMatch match , int semanticIndex , int branchIndex);
    void logout(Compiler compiler);
}
