package OrionLuouo.Craft.io.documents.fs3d.source.exception;

public interface Statement {
    String getWord();
    String getSentence();
    String getLocation();
    String getState();

    static String elementLocation(Statement statement) {
        return "\"" + statement.getWord() + '\"' +  "in the sentence " + '\"' + statement.getSentence() + '\"';
    }

    static String contextLocation(Statement statement) {
        return "Location: " + statement.getLocation() + '.';
    }
}