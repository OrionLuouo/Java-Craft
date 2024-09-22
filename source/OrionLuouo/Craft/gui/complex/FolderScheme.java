package OrionLuouo.Craft.gui.complex;

import OrionLuouo.Craft.data.Processor;
import OrionLuouo.Craft.gui.component.window.Window;
import OrionLuouo.Craft.gui.graphics.Colors;
import OrionLuouo.Craft.system.annotation.Unfinished;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class FolderScheme {
    @Unfinished
    static final Image DEFAULT_FOLDER_ICON , DEFAULT_ITEM_ICON , DEFAULT_FOLDED_FOLDER_ICON;

    @Unfinished
    static final MouseListener ROW_BAR_PANEL_LISTENER , ROW_BAR_LISTENER , COLUMN_BAR_PANEL_LISTENER , COLUMN_BAR_LISTENER;

    static final Processor<Item> DEFAULT_PROCESSOR = item -> {
        System.out.print("Item \"" + item.text.getText() + "\" is clicked! ");
        print(item.iPanel , "info");
    };

    static final Font DEFAULT_FONT;

    static final JLabel METRICS_LABEL;

    static {
        DEFAULT_FOLDER_ICON = null;
        DEFAULT_ITEM_ICON = null;
        DEFAULT_FOLDED_FOLDER_ICON = null;
        ROW_BAR_PANEL_LISTENER = new MouseAdapter() {
        };
        ROW_BAR_LISTENER = new MouseAdapter() {
        };
        COLUMN_BAR_PANEL_LISTENER = new MouseAdapter() {
        };
        COLUMN_BAR_LISTENER = new MouseAdapter() {
        };
        METRICS_LABEL = new JLabel();
        DEFAULT_FONT = METRICS_LABEL.getFont();
    }

    {
        itemIconListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                Item item = ((IconLabel) event.getComponent()).item;
                if (focus != null) {
                    focus.iPanel.setBackground(normColor);
                }
                focus = item;
                item.iPanel.setBackground(focusColor);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                Item item = ((IconLabel) event.getComponent()).item;
                processors.get(item.type).process(item);
            }
        };
        itemLabelListener = new MouseAdapter() {
            long timestamp;

            @Override
            public void mousePressed(MouseEvent event) {
                Item item = ((TextLabel) event.getComponent()).item;
                if (focus != null) {
                    focus.iPanel.setBackground(normColor);
                }
                focus = item;
                item.iPanel.setBackground(focusColor);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                long cache = System.currentTimeMillis();
                if (cache - timestamp < 500) {
                    Item item = ((TextLabel) event.getComponent()).item;
                    processors.get(item.type).process(item);
                    timestamp = 0;
                }
                else {
                    timestamp = cache;
                }
            }
        };
        folderIconListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                Item item = ((IconLabel) event.getComponent()).item;
                if (focus != null) {
                    focus.iPanel.setBackground(normColor);
                }
                focus = item;
                item.iPanel.setBackground(focusColor);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                ((Folder) ((IconLabel) event.getComponent()).item).opposeState();
            }
        };
        folderLabelListener = new MouseAdapter() {
            long timestamp;

            @Override
            public void mousePressed(MouseEvent event) {
                Item item = ((TextLabel) event.getComponent()).item;
                if (focus != null) {
                    focus.iPanel.setBackground(normColor);
                }
                focus = item;
                item.iPanel.setBackground(focusColor);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                long cache = System.currentTimeMillis();
                if (cache - timestamp < 500) {
                    ((Folder) ((TextLabel) event.getComponent()).item).opposeState();
                    timestamp = 0;
                }
                else {
                    timestamp = cache;
                }
            }
        };
    }

    JPanel panel;
    MainPanel mainPanel;
    RowBarPanel rowBarPanel;
    ColumnBarPanel columnBarPanel;
    int itemSize , rowLength , columnLength;
    Map<Type , Image> icons;
    Map<Type , Image> foldedIcons;
    Map<String , Type> types;
    Map<Type , Processor<Item>> processors;
    Font font;
    FontMetrics metrics;
    Color normColor , foreColor , focusColor;
    final Folder root;
    MouseListener itemLabelListener , itemIconListener , folderLabelListener , folderIconListener;
    Item focus;

    class MainPanel extends JPanel {
        MainPanel() {
            super();
            setLayout(null);
            setLocation(0 , 0);
        }
    }

    @Unfinished
    class RowBarPanel extends JPanel {
        class RowBar extends JLabel {

        }

        RowBar bar;

        RowBarPanel() {
            super();
            setLayout(null);
            bar = new RowBar();
        }

        @Unfinished
        void visualise() {

        }
    }

    @Unfinished
    class ColumnBarPanel extends JPanel {
        class ColumnBar extends JLabel {

        }

        ColumnBar bar;

        ColumnBarPanel() {
            super();
            setLayout(null);
            bar = new ColumnBar();
        }

        @Unfinished
        void visualise() {

        }
    }

    public class Item {

        JPanel iPanel;
        JLabel text;
        IconLabel icon;
        Type type;
        int length;

        Item(Type type , String text) {
            length = metrics.stringWidth(text);
            this.type = type;
            iPanel = new JPanel();
            iPanel.setSize(rowLength , itemSize);
            iPanel.setLayout(null);
            iPanel.setBackground(normColor);
            this.text = new TextLabel(text , this);
            this.text.setSize(rowLength , itemSize);
            this.text.setForeground(foreColor);
            icon = new IconLabel(icons.get(type) , this);
            iPanel.add(this.text);
            iPanel.add(icon);
            addListeners();
            iPanel.setOpaque(true);
        }

        void addListeners() {
            icon.addMouseListener(itemIconListener);
            text.addMouseListener(itemLabelListener);
        }

        public boolean isFolder() {
            return false;
        }
    }

    public class Folder extends Item {
        List<Item> content;
        boolean folded;

        Folder(Type type , String text) {
            super(type , text);
            content = new ArrayList<>();
            folded = true;
        }

        @Override
        void addListeners() {
            icon.addMouseListener(folderIconListener);
            text.addMouseListener(folderLabelListener);
        }

        @Override
        public boolean isFolder() {
            return true;
        }

        public Item getItem(String text) {
            Iterator<Item> iterator = content.iterator();
            Item item;
            while (iterator.hasNext()) {
                item = iterator.next();
                if (item.text.getText().equals(text)) {
                    return item;
                }
            }
            return null;
        }

        public Iterator<Item> itemIterator() {
            return content.iterator();
        }

        public void deleteItem(String text) {
            Iterator<Item> iterator = content.iterator();
            Item item;
            int index = 0;
            while (iterator.hasNext()) {
                item = iterator.next();
                if (item.text.getText().equals(text)) {
                    content.remove(index);
                    reorganise();
                }
                index++;
            }
        }

        public void deleteItem(Item item) {
            Iterator<Item> iterator = content.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                if (iterator.next() == item) {
                    content.remove(index);
                    reorganise();
                }
                index++;
            }
        }

        public void opposeState() {
            folded = !folded;
            icon.image = folded ? foldedIcons.get(type) : icons.get(type);
            reorganise();
            iPanel.repaint(0 , 0 , iPanel.getWidth() , iPanel.getHeight());
        }

        public boolean isFolded() {
            return folded;
        }
    }

    static class TextLabel extends JLabel {
        Item item;

        TextLabel(String text , Item item) {
            super(text);
            this.item = item;
        }
    }

    class IconLabel extends JLabel {
        Image image;
        Item item;

        IconLabel(Image icon , Item item) {
            super();
            image = icon;
            this.item = item;
            setSize(itemSize , itemSize);
        }

        @Override
        public void paintComponent(Graphics graphics) {
            graphics.drawImage(image , 0 , 0 , itemSize , itemSize , null);
        }
    }

    public record Type(String name) {
        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof Type && name.equals(((Type) object).name);
        }
    }

    @Unfinished
    public FolderScheme() {
        rowLength = Window.SCREEN_WIDTH;
        icons = new HashMap<>();
        processors = new HashMap<>();
        types = new HashMap<>();
        foldedIcons = new HashMap<>();
        panel = new JPanel() {
            @Override
            public void setSize(int width , int height) {
                super.setSize(width , height);
                mainPanel.setSize(width , height);
            }
        };
        panel.setLayout(null);
        mainPanel = new MainPanel();
        rowBarPanel = new RowBarPanel();
        columnBarPanel = new ColumnBarPanel();
        rowBarPanel.addMouseListener(ROW_BAR_PANEL_LISTENER);
        rowBarPanel.bar.addMouseListener(ROW_BAR_LISTENER);
        columnBarPanel.addMouseListener(COLUMN_BAR_PANEL_LISTENER);
        columnBarPanel.addMouseListener(COLUMN_BAR_LISTENER);
        panel.add(mainPanel);
        panel.add(rowBarPanel);
        panel.add(columnBarPanel);
        font = DEFAULT_FONT;
        metrics = METRICS_LABEL.getFontMetrics(font);
        itemSize = (int) ((double) metrics.getHeight() / 0.8);
        root = new Folder(null, "");
        normColor = Colors.GRAY_DARK;
        foreColor = Colors.GRAY_LIGHT;
        focusColor = Colors.BLUE_DEEP;
        types.put("Folder", new Type("Folder"));
        types.put("Item", new Type("Item"));
        icons.put(types.get("Folder"), DEFAULT_FOLDER_ICON);
        icons.put(types.get("Item"), DEFAULT_ITEM_ICON);
        panel.setBackground(normColor);
        mainPanel.setBackground(normColor);
        columnBarPanel.setBackground(normColor);
        rowBarPanel.setBackground(normColor);
        panel.setOpaque(true);
        mainPanel.setOpaque(true);
        columnBarPanel.setOpaque(true);
        rowBarPanel.setOpaque(true);
        processors.put(types.get("Item") , DEFAULT_PROCESSOR);
        foldedIcons.put(types.get("Folder") , DEFAULT_FOLDED_FOLDER_ICON);
    }

    void reorganise() {
        synchronized (root) {
            rowLength = columnLength = 0;
            recursiveReorganise(root, 0);
            if (rowLength > panel.getWidth()) {
                rowBarPanel.visualise();
            } else {
                rowLength = panel.getWidth();
            }
            iterateContent(root, item -> {
                item.iPanel.setSize(rowLength, itemSize);
                item.text.setSize(rowLength, itemSize);
            });
            if (columnLength > panel.getHeight()) {
                columnBarPanel.visualise();
            }
        }
    }

    void recursiveReorganise(Folder folder , int retract) {
        Iterator<Item> iterator = folder.content.iterator();
        Item item;
        while (iterator.hasNext()) {
            item = iterator.next();
            item.iPanel.setLocation(0 , columnLength);
            item.icon.setLocation(retract , 0);
            item.text.setLocation(retract + itemSize , 0);
            item.iPanel.repaint();
            columnLength += itemSize;
            int length = retract + itemSize + item.length;
            rowLength = Math.max(rowLength, length);
            if (item.isFolder()) {
                folder = (Folder) item;
                if (!folder.folded) {
                    recursiveReorganise(folder , retract + itemSize);
                }
                else {
                    iterateContent(folder , i -> {
                        i.iPanel.setLocation(0 , -itemSize);
                    });
                }
            }
        }
    }

    private static void print(Component component , String info) {
        System.out.println("Component \"" + info + "\" " + component.getX() + " , " + component.getY() + " , " + component.getWidth() + " , " + component.getHeight());
    }

    void iterateItems(Processor<Item> processor) {
        iterateContent(root , processor);
    }

    void iterateItems(Processor<Item> processor , Type type) {
        iterateContent(root , item -> {
            if (item.type.equals(type)) {
                processor.process(item);
            }
        });
    }

    void iterateContent(Folder folder , Processor<Item> processor) {
        Iterator<Item> iterator = folder.content.iterator();
        Item item;
        while (iterator.hasNext()) {
            item = iterator.next();
            processor.process(item);
            if (item.isFolder()) {
                iterateContent((Folder) item , processor);
            }
        }
    }

    public Folder getRoot() {
        return root;
    }

    public void setLocation(int x , int y) {
        panel.setLocation(x , y);
    }

    public void setWidth(int width) {
        panel.setSize(width , panel.getHeight());
        synchronized (root) {
            reorganise();
        }
    }

    public void setHeight(int height) {
        panel.setSize(panel.getWidth() , height);
        if (height < columnLength) {
            columnBarPanel.visualise();
        }
    }

    public void setFont(Font font) {
        this.font = font;
        metrics = METRICS_LABEL.getFontMetrics(font);
        int size = metrics.getHeight();
        itemSize = (int) ((double) size / 0.8);
        synchronized (root) {
            iterateItems(item -> {
                int length = metrics.stringWidth(item.text.getText());
                item.iPanel.setSize(panel.getWidth(), itemSize);
                item.icon.setSize(itemSize, itemSize);
                item.text.setBounds(itemSize, 0, length, itemSize);
            });
        }
        reorganise();
    }

    public void setBounds(int x , int y , int width , int height) {
        panel.setLocation(x , y);
        setWidth(width);
        setHeight(height);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setForeground(Color color) {
        foreColor = color;
        iterateItems(item -> {
            item.text.setForeground(color);
        });
    }

    public void setBackground(Color color) {
        panel.setBackground(color);
        normColor = color;
        iterateItems(item -> {
            item.iPanel.setBackground(color);
        });
    }

    public void addNewItem(Folder folder , Type type , String text) {
        synchronized (root) {
            Item item;
            folder.content.add(item = new Item(type, text));
            mainPanel.add(item.iPanel);
            reorganise();
            item.iPanel.repaint();
        }
    }

    public void addNewFolder(Folder folder , Type type , String text) {
        synchronized (root) {
            Item item;
            folder.content.add(item = new Folder(type, text));
            panel.add(item.iPanel);
            reorganise();
        }
    }

    public void addType(Type type) {
        if (!icons.containsKey(type)) {
            types.put(type.name , type);
            icons.put(type , DEFAULT_ITEM_ICON);
            processors.put(type , DEFAULT_PROCESSOR);
        }
    }

    public void setType(Type type , Image icon) {
        boolean existed = types.containsKey(type.name);
        types.put(type.name , type);
        icons.put(type , icon);
        if (existed) {
            iterateItems(item -> {
                if (item.type.equals(type)) {
                    item.icon.image = icon;
                    item.icon.repaint();
                }
            });
        }
        else {
            processors.put(type , DEFAULT_PROCESSOR);
        }
    }

    public void setFolderType(Type type , Image icon , Image foldedIcon) {
        boolean existed = types.containsKey(type.name);
        types.put(type.name , type);
        icons.put(type , icon);
        foldedIcons.put(type , foldedIcon);
        if (existed) {
            iterateItems(item -> {
                if (item.isFolder()) {
                    Folder folder = (Folder) item;
                    if (item.type.equals(type)) {
                        item.icon.image = folder.folded ? foldedIcon : icon;
                        item.icon.repaint();
                    }
                }
            });
        }
    }

    public void setProcessor(Type type , Processor<Item> processor) {
        processors.put(type , processor);
    }
}