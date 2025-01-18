package OrionLuouo.test.Craft.logic.input.SDP;

import OrionLuouo.Craft.logic.input.SDP.StreamParserStandardSystem;
import OrionLuouo.Craft.logic.input.SDP.StructuredDocumentParser;

public class StructuredDocumentParserTest {
    public static void main(String[] args) {
        StructuredDocumentParser structuredDocumentParser = StructuredDocumentParser.constructStandardParser();
        structuredDocumentParser.setRegexLayer((value , type) -> {
            System.out.println("Element Type: " + type + ", Value: " + value);
        });
        structuredDocumentParser.input("\"\\\"qwq\\u124\"1.22.qwq");
        structuredDocumentParser.endInput();
    }
}
