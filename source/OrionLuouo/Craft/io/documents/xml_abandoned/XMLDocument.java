package OrionLuouo.Craft.io.documents.xml_abandoned;

import java.io.*;
import java.util.*;

public class XMLDocument {
    protected Map<String, String> properties;
    protected Label root;

    public XMLDocument() {
        properties = new LinkedHashMap<>();
        root = new Label("root");
    }

    public XMLDocument(XMLDocument source) {
        properties = new LinkedHashMap<>(source.properties);
        root = new Label(source.root);
    }

    /**
     * Parse an XML document file.
     * This method will read all the bytes in the file one time.
     * So it mustn't be larger than 2147483647 bytes.
     * And may occupy lots of RAM.
     * For big file,
     * you'd better use XMLParser.
     *
     * @param file
     * @return
     */
    public static XMLDocument parse(File file) throws IOException {
        FileReader reader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        reader.read(chars);
        return new XMLStreamAnalyser().input(chars).summarize();
    }

    public void saveTo(Writer writer) throws IOException {
        writer.write("<?xml ");
        Set<Map.Entry<String, String>> entries = properties.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            writer.write(entry.getKey() + " = \"" + entry.getValue() + "\" ");
        }
        writer.write("?>\n");
        root.saveTo(writer, "");
        writer.flush();
    }

    public void setProperty(String property, String value) {
        properties.put(property, value);
    }

    public String getProperty(String property) {
        return properties.get(property);
    }

    public String removeProperty(String property) {
        return properties.remove(property);
    }

    public Label getRoot() {
        return root;
    }

    public static class Label {
        static final String BLANK = "";

        String name, text;
        Map<String, String> properties;
        Map<String, Label> childLabels;

        public Label() {
            properties = new LinkedHashMap<>();
            childLabels = new LinkedHashMap<>();
            name = "empty-label";
            text = BLANK;
        }

        public Label(String name) {
            properties = new LinkedHashMap<>();
            childLabels = new LinkedHashMap<>();
            this.name = name;
            text = BLANK;
        }

        public Label(String name, Map<String, String> properties) {
            this.name = name;
            this.properties = properties;
            childLabels = new LinkedHashMap<>();
            text = BLANK;
        }

        /**
         * Deeply clone.
         *
         * @param source
         */
        public Label(Label source) {
            name = source.name;
            text = source.text;
            properties = new LinkedHashMap<>(source.properties);
            childLabels = new LinkedHashMap<>();
            Set<Map.Entry<String, Label>> set = source.childLabels.entrySet();
            Iterator<Map.Entry<String, Label>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Label> entry = iterator.next();
                childLabels.put(entry.getKey(), new Label(entry.getValue()));
            }
        }

        protected void saveTo(Writer writer, String begin) throws IOException {
            String b = begin + (char) 9;
            writer.write(begin + "<" + name + " ");
            Set<Map.Entry<String, String>> entries = properties.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                writer.write(entry.getKey() + " = \"" + entry.getValue() + "\" ");
            }
            if (text.equals(BLANK) && childLabels.isEmpty()) {
                writer.write("/>\n");
                return;
            }
            writer.write(">\n");
            writer.write(b + text + "\n");
            Collection<Label> labels = childLabels.values();
            for (Label label : labels)
                label.saveTo(writer, b);
            writer.write(begin + "</" + name + ">");
        }

        public void setProperty(String property, String value) {
            properties.put(property, value);
        }

        public String getProperty(String property) {
            return properties.get(property);
        }

        public String removeProperty(String property) {
            return properties.remove(property);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void addLabel(Label label) {
            childLabels.put(label.name, label);
        }

        public Label removeLabel(String name) {
            return childLabels.remove(name);
        }

        public Label getLabel(String name) {
            return childLabels.get(name);
        }

        public Label[] getLabels() {
            return childLabels.values().toArray(new Label[childLabels.size()]);
        }

        public Label[] filterLabels(String property, String value) {
            LinkedList<Label> list = new LinkedList<>();
            Collection<Label> labels = childLabels.values();
            for (Label label : labels) {
                String v = label.properties.get(property);
                if (v != null && v.equals(value))
                    list.add(label);
            }
            return list.toArray(new Label[list.size()]);
        }

        public Label[] filterLabels(String[] properties, String[] values) {
            LinkedList<Label> list = new LinkedList<>();
            Collection<Label> labels = childLabels.values();
            lp:
            for (Label label : labels) {
                for (int i = 0; i < properties.length; i++) {
                    String v = label.properties.get(properties[i]);
                    if (v == null || !v.equals(values[i]))
                        continue lp;
                }
                list.add(label);
            }
            return list.toArray(new Label[list.size()]);
        }

        public Label[] removeLabels(String property, String value) {
            LinkedList<Label> list = new LinkedList<>();
            Collection<Label> labels = childLabels.values();
            for (Label label : labels) {
                String v = label.properties.get(property);
                if (v != null && v.equals(value)) {
                    list.add(label);
                    childLabels.remove(label.name);
                }
            }
            return list.toArray(new Label[list.size()]);
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
