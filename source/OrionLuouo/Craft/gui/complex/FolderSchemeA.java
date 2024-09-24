package OrionLuouo.Craft.gui.complex;

import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.Processor;
import OrionLuouo.Craft.gui.graphics.Colors;
import OrionLuouo.Craft.gui.listener.mouseListener.DoubleClickedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class FolderSchemeA {
    /** deleted an important component by mistake : DictionaryMap<V>
    public static final ImageIcon DEFAULT_FOLDER_IMAGE = null
            , DEFAULT_ITEM_IMAGE = null;
    public static final int DEFAULT_ITEM_SIZE = 20
            , DEFAULT_BAR_SIZE = 10;
    final MouseListener ITEM_LISTENER = new DoubleClickedListener() {
        @Override
        public void doubleClicked(MouseEvent e) {
            Item item = ((Item.ItemLabel) (e.getComponent())).item;
            item.event.process(item);
        }

        @Override
        public void mousePressed(MouseEvent event) {
            if (focus != null) {
                
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {

        }
    }
    , ICON_LISTENER = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent event) {

        }
    };

    public record Type(String name) {

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object type) {
            return type instanceof Type && name.equals(((Type) type).name);
        }
    }

    public class Item {
        String text;
        JPanel panel;
        JLabel icon , label;
        Type type;
        Processor<Item> event;

        static class ItemLabel extends JLabel {
            Item item;

            ItemLabel(Item item) {
                super();
                this.item = item;
            }
        }

        public Item(String text , Type type) {
            this.text = text;
            icon = (icon = (JLabel) icons.get(type)) == null ? new JLabel() : icon;
            panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(normColor);
            int stringWidth;
            panel.setSize((stringWidth = fontMetrics.stringWidth(text)) + itemSize , itemSize);
            icon = new ItemLabel(this);
            icon.addMouseListener(ICON_LISTENER);
            icon.setBounds(0 , 0 , itemSize , itemSize);
            icon.setIcon(null);
        }

        boolean isFolder() {
            return false;
        }

        public void setIcon(ImageIcon icon) {
            panel.remove(this.icon);
            this.icon = new JLabel(icon);
            this.icon.setBounds(0 , 0 , itemSize, itemSize);
            panel.add(this.icon);
        }

        void setIcon(JLabel label) {
            panel.remove(icon);
            icon = label;
            panel.add(label);
        }
    }

    public class Folder extends Item {
        static final Processor<Item> FOLDER_EVENT = folder -> {
            ((Folder) folder).opposeState();
        };

        OrionLuouo.Craft.data.container.Map<String , Item> contentList;
        boolean unfolded;

        static class ItemIterator implements Iterator<Item> {
            Iterator<OrionLuouo.Craft.data.container.Map.Entry<String , Item>> iterator;

            ItemIterator(Iterator<OrionLuouo.Craft.data.container.Map.Entry<String , Item>> it) {
                iterator = it;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Item next() {
                return iterator.next().value();
            }
        }

        public Folder(String text) {
            super(text , types.get("Folder"));
            contentList = new Dictionary<>();
            event = FOLDER_EVENT;
        }

        @Override
        public boolean isFolder() {
            return true;
        }

        public Iterator<Item> iterateContent() {
            return new ItemIterator(contentList.iterator());
        }

        public Iterator<Item> iterateItems() {
            return new Iterator<Item>() {
                Iterator<Item> iterator = new ItemIterator(contentList.iterator());
                Item cache;

                @Override
                public boolean hasNext() {
                    while(iterator.hasNext()) {
                        cache = iterator.next();
                        if (!cache.isFolder()) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public Item next() {
                    return cache;
                }
            };
        }

        public Iterator<Folder> iterateFolders() {
            return new Iterator<Folder>() {
                Iterator<Item> iterator = new ItemIterator(contentList.iterator());
                Item cache;

                @Override
                public boolean hasNext() {
                    while(iterator.hasNext()) {
                        cache = iterator.next();
                        if (cache.isFolder()) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public Folder next() {
                    return (Folder) cache;
                }
            };
        }

        public void addItem(Item item) {
            contentList.add(item.text , item);
            reorganise();
        }

        public void deleteItem(Item item) {
            contentList.remove(item.text);
            reorganise();
        }

        public void opposeState() {
            unfolded = !unfolded;
            reorganise();
        }
    }

    int itemSize , maxLength , height , rowBarEnd , columnBarEnd , barSize;
    JPanel panel , mainPanel , rowBarPanel , columnBarPanel;
    Folder root;
    Map<Type , Icon> icons;
    Map<String , Type> types;
    Item focus;
    Color normColor;
    Font font;
    FontMetrics fontMetrics;

    public class HorizontalDragListener extends MouseAdapter {
        int mouseX;
        Component component;

        public HorizontalDragListener(Component component){
            component.addMouseListener(this);
            component.addMouseMotionListener(this);
            this.component = component;
        }

        public void mousePressed(MouseEvent e) {
            mouseX = e.getX();
        }

        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            component.setLocation(component.getX() + x - mouseX, component.getY());
            mainPanel.setLocation((int) (((float) x / maxLength) * rowBarEnd) , mainPanel.getY());
            if (x == maxLength) {
                mainPanel.setLocation(rowBarEnd , mainPanel.getY());
            }
            else if (x == 0) {
                mainPanel.setLocation(0 , mainPanel.getY());
            }
        }
    }

    public class VerticalDragListener extends MouseAdapter {
        int mouseY;
        Component component;

        public VerticalDragListener(Component component){
            component.addMouseListener(this);
            component.addMouseMotionListener(this);
            this.component = component;
        }

        public void mousePressed(MouseEvent e) {
            mouseY = e.getY();
        }

        public void mouseDragged(MouseEvent e) {
            int y = e.getY();
            component.setLocation(component.getX(), component.getY() + y - mouseY);
            mainPanel.setLocation(mainPanel.getX() , (int) (((float) y / maxLength) * rowBarEnd));
            if (y == maxLength) {
                mainPanel.setLocation(mainPanel.getX() , columnBarEnd);
            }
            else if (y == 0) {
                mainPanel.setLocation(mainPanel.getX() , 0);
            }
        }
    }

    class RowBarPanel extends JPanel {
        HorizontalDragListener listener;
        JLabel bar;

        RowBarPanel() {
            super();
            setLayout(null);
            setVisible(false);
            bar = new JLabel();
            listener = new HorizontalDragListener(bar);
        }

        @Override
        public void setSize(int width , int height) {
            super.setSize(width , height);
        }
    }

    class ColumnBarPanel extends JPanel {
        VerticalDragListener listener;
        JLabel bar;

        ColumnBarPanel() {
            super();
            setLayout(null);
            setVisible(false);
            bar = new JLabel();
            listener = new VerticalDragListener(bar);
        }

        @Override
        public void setSize(int width , int height) {
            super.setSize(width , height);

        }
    }

    public FolderSchemeA() {
        icons = new HashMap<>();
        types = new HashMap<>();
        Type type = new Type("Folder");
        types.put("Folder" , type);
        icons.put(type , DEFAULT_FOLDER_IMAGE);
        type = new Type("Item");
        types.put("Item" , type);
        icons.put(type , DEFAULT_ITEM_IMAGE);
        mainPanel = new JPanel();
        mainPanel.setLocation(0 , 0);
        mainPanel.setLayout(null);
        panel.add(mainPanel);
        panel.add(rowBarPanel = new RowBarPanel());
        panel.add(columnBarPanel = new ColumnBarPanel());
        panel.setBackground(normColor = Colors.GRAY_DARK);
        ((ColumnBarPanel) columnBarPanel).bar.setBackground(Colors.GRAY_DEEP);
        ((RowBarPanel) rowBarPanel).bar.setBackground(Colors.GRAY_DEEP);
        barSize = DEFAULT_BAR_SIZE;
        itemSize = DEFAULT_ITEM_SIZE;
    }

    void reorganise() {
        maxLength = 0;
        height = 0;
        organise(root , 0);
        if (height > panel.getHeight()) {
            columnBarPanel.setVisible(true);
        }
        if (maxLength > panel.getWidth()) {
            rowBarPanel.setVisible(true);
        }
    }

    void organise(Item item , int retract) {
        int length = retract + item.panel.getWidth();
        maxLength = maxLength > length ? maxLength : length;
        if (!item.isFolder()) {
            item.panel.setLocation(retract , height);
            height += itemSize;
            return;
        }
        item.panel.setLocation(retract , height);
        height += itemSize;
        retract += itemSize;
        Iterator<Item> iterator = ((Folder) item).iterateContent();
        while (iterator.hasNext()) {
            organise(iterator.next() , retract);
        }
    }

    void iterateItems(Folder folder , Processor<Folder> folderProcessor , Processor<Item> itemProcessor) {
        folderProcessor.process(folder);
        Iterator<Item> iterator = folder.iterateContent();
        Item item;
        while (iterator.hasNext()) {
            if ((item = iterator.next()).isFolder()) {
                iterateItems((Folder) item , folderProcessor , itemProcessor);
                continue;
            }
            itemProcessor.process(item);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setBounds(int x , int y , int width , int height) {
        panel.setLocation(x , y);
        setWidth(width);
        setHeight(height);
    }

    public void setLocation(int x , int y) {
        panel.setLocation(x , y);
    }

    public void setWidth(int width) {
        panel.setSize(width , panel.getHeight());
        columnBarPanel.setLocation(width - barSize , 1);
        checkRowBar();
    }

    public void setHeight(int height) {
        panel.setSize(panel.getWidth() , height);
        rowBarPanel.setLocation(1 , height - barSize);
        checkColumnBar();
    }

    void checkRowBar() {
        columnBarPanel.setLocation(panel.getWidth() - columnBarPanel.getWidth() , 0);
        if (panel.getWidth() < maxLength) {
            rowBarPanel.setVisible(false);
            return;
        }
        rowBarPanel.setSize(mainPanel.getWidth() - barSize - 2 , barSize);
        rowBarPanel.setVisible(true);
    }

    void checkColumnBar() {
        if (panel.getHeight() < height) {
            columnBarPanel.setVisible(false);
            return;
        }
        columnBarPanel.setSize(barSize , mainPanel.getHeight() - 2);
        columnBarPanel.setVisible(true);
    }

    public Folder getRoot() {
        return root;
    }

    public void setIcon(Type type , ImageIcon icon) {
        JLabel label;
        if (icon.getIconWidth() == itemSize && icon.getIconHeight() == itemSize) {
            label = null;
            icons.put(type , icon);
        }
        else {
            label = new JLabel() {
                Image image = icon.getImage();

                @Override
                public void paintComponent(Graphics graphics) {
                    graphics.drawImage(image , 0 , 0 , itemSize , itemSize , null);
                }
            };
        }
        iterateItems(root , folder -> {
            if (folder.type.equals(type)) {
                folder.setIcon(label);
            }
        } , item -> {
            item.setIcon(label);
        });
    }

    public void setBackground(Color color) {
        panel.setBackground(color);
    }

    public void setBarColor(Color color) {
        ((RowBarPanel) rowBarPanel).setBackground(color);
        ((ColumnBarPanel) columnBarPanel).setBackground(color);
    }

    public void setFont(Font font) {

    }*/
}