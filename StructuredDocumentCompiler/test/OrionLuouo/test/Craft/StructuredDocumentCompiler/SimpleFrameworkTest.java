package OrionLuouo.test.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.Compiler;
import OrionLuouo.Craft.StructuredDocumentCompiler.SemanticMatch;
import OrionLuouo.Craft.StructuredDocumentCompiler.SemanticRegex;
import OrionLuouo.Craft.StructuredDocumentCompiler.StructureLayer;

public class SimpleFrameworkTest {
    public static final String TEST_CONTENT = "&&^^%";

    public static void main(String[] args) {
        Compiler compiler = new Compiler();
        SemanticRegex regex = SemanticRegex.compile(compiler , TEST_CONTENT);
        regex.addSemantics(1);
        compiler.registerRegex(regex , "test");
        compiler.loadRegex("test");
        compiler.coverLayer(new StructureLayer() {
            @Override
            public void reload(Compiler compiler) {
                System.out.println("> StructureLayer loaded!");
            }

            @Override
            public void parseSemantics(SemanticMatch match) {
                System.out.println("> Regex matched!");
                compiler.retractLayer();
            }

            @Override
            public void logout(Compiler compiler) {
                System.out.println("> StructureLayer logout!");
            }
        });
        compiler.input("&&^^%");
    }
}
