package OrionLuouo.Craft.io.documents.xml;

import java.util.Map;

public interface XMLAnalyser {
    XMLDocument summarize();

    class Document extends XMLDocument {
        public Document(Map<String, String> properties, Label root) {
            this.properties = properties;
            this.root = root;
        }
    }
}
