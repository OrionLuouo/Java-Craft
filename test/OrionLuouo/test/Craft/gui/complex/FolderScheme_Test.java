package OrionLuouo.test.Craft.gui.complex;

import OrionLuouo.Craft.gui.complex.FolderScheme;
import OrionLuouo.Craft.gui.component.window.Window;
import OrionLuouo.Craft.gui.graphics.Colors;

public class FolderScheme_Test {
    static Window window = new Window(Window.SIZE_MIDDLE);

    static FolderScheme folderScheme = new FolderScheme();

    public static void main(String[] args) {
        window.setExitOnClose();
        window.setBackground(Colors.PURPLE);
        window.add(folderScheme.getPanel());
        folderScheme.setBounds(0 , 0 , window.getWidth() / 2 , window.getHeight() / 2);
        folderScheme.addNewItem(folderScheme.getRoot() , new FolderScheme.Type("Item") , "qwq");
        folderScheme.getPanel().setBackground(Colors.GRAY_DEEP);

        FolderScheme.Folder folder = folderScheme.getRoot();
        folderScheme.addNewFolder(folder , new FolderScheme.Type("Folder") , "qaq");
        folder = (FolderScheme.Folder) folder.getItem("qaq");
        folderScheme.addNewItem(folder , new FolderScheme.Type("Item") , "aaaaaaa");
        folder.opposeState();
        folderScheme.addNewFolder(folder , new FolderScheme.Type("Folder") , "fol");
        folder = (FolderScheme.Folder) folder.getItem("fol");
        folderScheme.addNewItem(folder , new FolderScheme.Type("Item") , "ite");
    }
}
