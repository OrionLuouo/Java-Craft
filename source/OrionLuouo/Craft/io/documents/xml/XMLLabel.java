package OrionLuouo.Craft.io.documents.xml;

public interface XMLLabel {
    void dropAttribute(String name);
    void dropAttribute(int index);
    void insertAttribute(String name , String value , String surAttributeName);
    void insertAttribute(String name , String value , int index);
    void addAttribute(String name , String value);
    int locateAttribute(String name);
    String getAttributeValue(String name);
    String getAttributeValue(int index);
    XMLLabel getLabel(String name);
    XMLLabel getLabel(int index);
    int getAttributeCount();
    int getLabelCount();
    int locateLabel(String name);
    void dropLabel(String name);
    void dropLabel(int index);
    void insertLabel(String name , String surLabelName);
    void insertLabel(String name , int index);
    void addLabel(String name);
}
