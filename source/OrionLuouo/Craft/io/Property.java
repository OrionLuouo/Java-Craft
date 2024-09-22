package OrionLuouo.Craft.io;

import java.io.*;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Property {
    protected HashMap<String, String> entryMap = new HashMap<>();
    protected java.io.File file;

    public void load(File file) throws IOException {
        if (file == null || !file.exists() || file.isDirectory())
            return;
        this.file = file;
        byte[] bytes = new byte[(int) file.length()];
        new FileInputStream(file).read(bytes);
        String text = new String(bytes);
        String[] entries = text.split("\n");
        for (String entry : entries) {
            if (entry.endsWith("\r"))
                entry = entry.substring(0, entry.length() - 1);
            try {
                String[] e = entry.split("=", 2);
                entryMap.put(e[0], e[1]);
            } catch (Exception ignored) {
            }
        }
    }

    public String getProperty(String key) {
        return entryMap.get(key);
    }

    public void removeValue(String key) {
        entryMap.put(key, null);
    }

    public void removeKey(String key) {
        entryMap.remove(key);
    }

    public void setProperty(String key, String value) {
        entryMap.put(key, value);
    }

    public int getKeyCount() {
        return entryMap.size();
    }

    public boolean isEmpty() {
        return entryMap.isEmpty();
    }

    public void clear() {
        entryMap = new HashMap<>();
        file = null;
    }

    public boolean containsKey(String key) {
        return entryMap.containsKey(key);
    }

    public void save() {
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileWriter writer = new FileWriter(file)) {
            Set<Map.Entry<String, String>> entries = entryMap.entrySet();
            for (Map.Entry<String, String> entry : entries)
                writer.write(entry.getKey() + '=' + entry.getValue() + '\n');
            writer.flush();
        } catch (IOException ignored) {
        }
    }

    public void saveTo(OutputStream stream) {
        try(Writer writer = new OutputStreamWriter(stream)){
            Set<Map.Entry<String, String>> entries = entryMap.entrySet();
            for (Map.Entry<String, String> entry : entries)
                writer.write(entry.getKey() + '=' + entry.getValue() + '\n');
            writer.flush();
        }catch (IOException ignored){
        }
    }

    public Set<String> keySet(){
        return entryMap.keySet();
    }

    public Collection<String> valueCollection(){
        return entryMap.values();
    }
}
