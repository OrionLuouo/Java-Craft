package OrionLuouo.test.Craft;

import java.io.*;

public class Merge {
    static char[] buffer = new char[65536];

    public static void main(String[] args) throws IOException {
        //merge(".out.other.fs3dMerge".replace("." , "\\\\") , ".source.OrionLuouo.Craft.io.documents.fs3d".replace("." , "\\\\"));
        merge("\\out\\other\\CraftMerge" , "\\source\\OrionLuouo\\Craft");
    }
    
    public static void merge(String fileName , String source) throws IOException {
        String rootPath = System.getProperty("user.dir");
        File destination = new File(rootPath + fileName);
        destination.createNewFile();
        Writer writer = new FileWriter(destination);
        copy(new File(rootPath + source), writer);
    }

    static void copy(File source , Writer destination) throws IOException {
        destination.write(source.getName() + "\n");
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            for (File file : files) {
                copy(file, destination);
            }
            return;
        }
        Reader reader = new FileReader(source);
        int count;
        while ((count = reader.read(buffer)) != -1) {
            destination.write(buffer , 0, count);
        }
        destination.write('\n');
        System.out.println(">File \"" + source.getAbsolutePath().replace("\\\\" , ".") + "\" appended.");
    }
}
