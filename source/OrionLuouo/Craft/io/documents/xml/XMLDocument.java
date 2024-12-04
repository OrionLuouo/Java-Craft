package OrionLuouo.Craft.io.documents.xml;

import OrionLuouo.Craft.io.File;

import java.io.*;

public interface XMLDocument {
    default void saveTo(File file) throws IOException {
        saveTo(new FileWriter(file));
    }

    default void saveTo(OutputStream stream) {
        saveTo(new OutputStreamWriter(stream));
    }

    void saveTo(Writer writer);

}
