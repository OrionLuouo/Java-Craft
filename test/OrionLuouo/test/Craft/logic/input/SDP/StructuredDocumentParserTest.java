package OrionLuouo.test.Craft.logic.input.SDP;

import OrionLuouo.Craft.logic.input.SDP.StreamParserStandardSystem;
import OrionLuouo.Craft.logic.input.SDP.StructuredDocumentParser;

public class StructuredDocumentParserTest {
    public static void main(String[] args) {
        StructuredDocumentParser structuredDocumentParser = new StructuredDocumentParser();
        StreamParserStandardSystem streamParserStandardSystem = new StreamParserStandardSystem(structuredDocumentParser);
        structuredDocumentParser.initializeStreamParser(streamParserStandardSystem.getStreamParser());
        structuredDocumentParser.getWordParser().addBasicMarkTypes();
        structuredDocumentParser.input("/***/\"qwq\"...");
        structuredDocumentParser.input("QAQ?>>\"QAQ?\"//////a\ra/");
        structuredDocumentParser.endInput();
    }
}
