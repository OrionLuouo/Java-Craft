package OrionLuouo.Craft.logic.input.SDP;

public class GrammarParser {
    void input(Object element , WordParser.WordType type) {
        System.out.println("GrammarParser accepted: <" + element.getClass() + ':' + element + ", " + type.name() + ">");
    }
}
